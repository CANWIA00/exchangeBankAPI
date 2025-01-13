package com.canwia.BankExchange.service;


import com.canwia.BankExchange.auth.AuthUtil;
import com.canwia.BankExchange.dto.ExchangeDto;
import com.canwia.BankExchange.dto.converter.ExchangeDtoConverter;
import com.canwia.BankExchange.dto.requests.ExchangeRequest;
import com.canwia.BankExchange.exception.CustomException;
import com.canwia.BankExchange.model.*;
import com.canwia.BankExchange.model.Currency;
import com.canwia.BankExchange.repository.ExchangeRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class ExchangeService {
    private final ExchangeRepository exchangeRepository;
    private final AccountService accountService;



    private final AmqpTemplate rabbitTemplate;
    private final DirectExchange directExchange;
    private final CurrencyService currencyService;
    private final ExchangeDtoConverter exchangeDtoConverter;
    private final UserService userService;

    @Value("${sample.rabbitmq.routingKey}")
    String routingKey1;

    @Value("${sample.rabbitmq.queue}")
    String queueName;


    public ExchangeService(ExchangeRepository exchangeRepository, AccountService accountService, AmqpTemplate rabbitTemplate, DirectExchange directExchange, CurrencyService currencyService, ExchangeDtoConverter exchangeDtoConverter, UserService userService) {
        this.exchangeRepository = exchangeRepository;
        this.accountService = accountService;
        this.rabbitTemplate = rabbitTemplate;
        this.directExchange = directExchange;
        this.currencyService = currencyService;
        this.exchangeDtoConverter = exchangeDtoConverter;
        this.userService = userService;
    }

    //Directly, send to the rabbitMQ our request and our first step going to read our message then all the process going to start.
    public void createExchange(ExchangeRequest exchangeRequest) {

        Operation operation = Operation.valueOf(exchangeRequest.getOperation());
        Optional<Account> plnAccount = accountService.findAccountById(UUID.fromString(exchangeRequest.getPlnAccount_id()));
        Optional<Account> otherAccount = accountService.findAccountById(UUID.fromString(exchangeRequest.getOtherAccount_id()));
        BigDecimal neededAmount = calculateNeededAmountForExchangeBuy(exchangeRequest.getAmount(), otherAccount.get().getCurrencyCode());
        Currency currency = currencyService.findCurrencyById(otherAccount.get().getCurrencyCode());

        if (plnAccount.isEmpty()) {
            System.out.println("Account not found for the provided IDs.");
        }

        if(neededAmount == null){
            System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
        }

        if (currency == null) {
            System.out.println("Currency not found for the given account.");
        }
        if(operation==Operation.BUY){
            rabbitTemplate.convertAndSend(directExchange.getName(), routingKey1, exchangeRequest);
        } else if (operation==Operation.SELL) {
            rabbitTemplate.convertAndSend(directExchange.getName(),"fourthRoute",exchangeRequest);
        }else {
            System.out.println("Operation exception!!!");
        }
    }


    //*** First-Step ***
    // Get account from account service.
    // Calculate how much do we need to subtract for withdraw amount.
    // Compare if is there enough money in from_Account
    // Send it to the second_queue
    @RabbitListener(queues = "${sample.rabbitmq.queue}")
    @Transactional
    public void fromAccountProcess_ExchangeBuy(ExchangeRequest exchangeRequest) {
        Optional<Account> account_PLN = accountService.findAccountById(UUID.fromString(exchangeRequest.getPlnAccount_id()));
        Optional<Account> account_OTHER = accountService.findAccountById(UUID.fromString(exchangeRequest.getOtherAccount_id()));
        BigDecimal neededAmount = calculateNeededAmountForExchangeBuy(exchangeRequest.getAmount(), account_OTHER.get().getCurrencyCode());


        if (account_PLN.isPresent()) {
            if(ifEnoughAmount(neededAmount,account_PLN.get().getBalance())){
                subtractFromAccount(neededAmount,account_PLN.get());
                rabbitTemplate.convertAndSend(directExchange.getName(), "secondRoute", exchangeRequest);
            }else {
                System.out.println("Internal exception - (fromAccountProcess_BuyExchange); Not enough balance or invalid currency! " + account_PLN.get().getId());
            }
        }else {
            System.out.println("Internal exception - (fromAccountProcess_BuyExchange); Account && Currency not found!");
        }

    }


    ///*** Second-Step ***///
    // Check is that valid to_account
    // Add money to_account and save
    // if not; charge money back to the sender account
    // Send it to the third_queue
    @RabbitListener(queues = "secondStepQueue")
    @Transactional
    public void toAccountProcess_ExchangeBuy(ExchangeRequest buyExchangeRequest) {
        Optional<Account> accountOther = accountService.findAccountById(UUID.fromString(buyExchangeRequest.getOtherAccount_id()));
        Optional<Account> accountPLN = accountService.findAccountById(UUID.fromString(buyExchangeRequest.getPlnAccount_id()));

        accountOther.ifPresentOrElse( account -> {
                    addToAccount(buyExchangeRequest.getAmount(), account);
                    rabbitTemplate.convertAndSend(directExchange.getName(), "thirdRoute", buyExchangeRequest);
                },
                ()->{
                    System.out.println("Internal exception - (toAccountProcess_BuyExchange); Account not Found! " + accountOther.get().getId());
                    BigDecimal neededAmount = calculateNeededAmountForExchangeBuy(buyExchangeRequest.getAmount(),accountOther.get().getCurrencyCode());
                    addToAccount(neededAmount, accountPLN.get());
                }

        );
    }

    ///*** Third-Step ***//
    // save exchange to exchange repo
    // finalize transfer and make the notification
    @RabbitListener(queues = "thirdStepQueue")
    @Transactional
    public void finalizeExchangeBUY(ExchangeRequest exchangeRequest) {
        Optional<Account> pln_AccountOptional = accountService.findAccountById(UUID.fromString(exchangeRequest.getPlnAccount_id()));
        Optional<Account> other_AccountOptional = accountService.findAccountById(UUID.fromString(exchangeRequest.getOtherAccount_id()));

        BigDecimal currencyRate_BUY = BigDecimal.valueOf(currencyService.findCurrencyById(other_AccountOptional.get().getCurrencyCode()).getBuy());

        notification(pln_AccountOptional,other_AccountOptional);



        Exchange exchange = new Exchange();
        exchange.setAccount(other_AccountOptional.get());
        exchange.setPlnAccountId(pln_AccountOptional.get().getId());
        exchange.setOperation(Operation.BUY);
        exchange.setExchangeRate(currencyRate_BUY);
        exchange.setPlnAmount(calculateNeededAmountForExchangeBuy(exchangeRequest.getAmount(), other_AccountOptional.get().getCurrencyCode()));
        exchange.setOtherCurrencyAmount(exchangeRequest.getAmount());
        exchange.setPlnCurrency("PLN");
        exchange.setOtherCurrency(other_AccountOptional.get().getCurrencyCode());
        exchange.setTransactionFee(BigDecimal.ZERO);
        exchange.setExchangeDate(LocalDateTime.now());
        exchangeRepository.save(exchange);

    }



    @RabbitListener(queues = "fourthStepQueue")
    @Transactional
    public void fromAccount_ExchangeSell(ExchangeRequest exchangeRequest) {
        Optional<Account> accountOther = accountService.findAccountById(UUID.fromString(exchangeRequest.getOtherAccount_id()));


        if (accountOther.isPresent() || exchangeRequest.getAmount()!= null) {
            if (ifEnoughAmount(exchangeRequest.getAmount(), accountOther.get().getBalance())) {
                log.info("Before subtraction: {}", accountOther.get().getBalance());
                subtractFromAccount(exchangeRequest.getAmount(), accountOther.get());
                log.info("After subtraction: {}", accountOther.get().getBalance());

                rabbitTemplate.convertAndSend(directExchange.getName(), "fifthRoute", exchangeRequest);
            } else {
                log.info("Not enough balance in account: {}", exchangeRequest.getOtherAccount_id());
            }
        } else {
            log.info("Account not found: {}", exchangeRequest.getOtherAccount_id());
        }

    }


    @RabbitListener(queues = "fifthStepQueue")
    @Transactional
    public void toAccount_ExchangeSell(ExchangeRequest exchangeRequest) {

        Optional<Account> accountOther = accountService.findAccountById(UUID.fromString(exchangeRequest.getOtherAccount_id()));
        Optional<Account> accountPLN = accountService.findAccountById(UUID.fromString(exchangeRequest.getPlnAccount_id()));
        BigDecimal amount = calculateNeededAmountForExchangeSell(exchangeRequest.getAmount(), accountOther.get().getCurrencyCode());


        accountPLN.ifPresentOrElse( account -> {
                    addToAccount(amount, account);
                    rabbitTemplate.convertAndSend(directExchange.getName(), "sixthRoute", exchangeRequest);
                },
                ()->{
                    System.out.println("Internal exception - (toAccountProcess_BuyExchange); Not enough balance! " + accountOther.get().getId());
                    BigDecimal neededAmount = calculateNeededAmountForExchangeBuy(exchangeRequest.getAmount(), accountOther.get().getCurrencyCode());
                    addToAccount(neededAmount, accountOther.get());
                }

        );
    }

    @RabbitListener(queues = "sixthStepQueue")
    @Transactional
    public void finalizeExchangeSELL(ExchangeRequest exchangeRequest) {
        Optional<Account> pln_AccountOptional = accountService.findAccountById(UUID.fromString(exchangeRequest.getPlnAccount_id()));
        Optional<Account> other_AccountOptional = accountService.findAccountById(UUID.fromString(exchangeRequest.getOtherAccount_id()));

        BigDecimal currencyRate_SELL = BigDecimal.valueOf(currencyService.findCurrencyById(other_AccountOptional.get().getCurrencyCode()).getSell());

        notification(pln_AccountOptional,other_AccountOptional);



        Exchange exchange = new Exchange();
        exchange.setAccount(other_AccountOptional.get());
        exchange.setPlnAccountId(pln_AccountOptional.get().getId());

        exchange.setOperation(Operation.SELL);
        exchange.setExchangeRate(currencyRate_SELL);
        exchange.setPlnAmount(calculateNeededAmountForExchangeSell(exchangeRequest.getAmount(), other_AccountOptional.get().getCurrencyCode()));
        exchange.setOtherCurrencyAmount(exchangeRequest.getAmount());
        exchange.setPlnCurrency("PLN");
        exchange.setOtherCurrency(other_AccountOptional.get().getCurrencyCode());
        exchange.setTransactionFee(BigDecimal.ZERO);
        exchange.setExchangeDate(LocalDateTime.now());
        exchangeRepository.save(exchange);

    }




    public List<ExchangeDto> getAllExchangeByUserId() {
        List<Account> accounts = accountService.getAllAccountByUser();

        List<UUID> accountIds = accounts.stream().map(Account::getId).toList();

        List<ExchangeDto> allExchanges = accountIds.stream()
                .flatMap(accountId -> getAllExchangeByAccountId(accountId.toString()).stream())
                .collect(Collectors.toList());

        return allExchanges;

    }


    public List<ExchangeDto> getAllExchangeByAccountId(String id) {
        return exchangeDtoConverter.convertFrom(exchangeRepository.findAllByAccountId(UUID.fromString(id)));
    }

    public ExchangeDto getExchangeById(String id) {
        return exchangeDtoConverter.convertFrom(exchangeRepository.findById(UUID.fromString(id)).orElseThrow(() -> new CustomException("Exchange not found! @@ExchangeService:v35")));
    }


    /****************************************** Helper Methods ********************************************/

    protected void notification(Optional<Account> plnAccount, Optional<Account> otherAccount) {
        plnAccount.ifPresentOrElse(account -> {
                    System.out.println("PLN Account: " + account.getId()+ " new account balance: " + account.getBalance());
                },
                ()-> System.out.println("Account not found!")
        );
        otherAccount.ifPresentOrElse(account -> {
                    System.out.println("OTHER Account: " + account.getId()+ " new account balance: " + account.getBalance());
                },
                ()-> System.out.println("Account not found!")
        );
    }

    protected BigDecimal calculateNeededAmountForExchangeBuy(BigDecimal amount, String currencyCode) {

        BigDecimal currencyRate = BigDecimal.valueOf(currencyService.findCurrencyById(currencyCode).getBuy());
        return amount.multiply(currencyRate);
    }

    protected BigDecimal calculateNeededAmountForExchangeSell(BigDecimal amount, String currencyCode) {
        BigDecimal currencyRate = BigDecimal.valueOf(currencyService.findCurrencyById(currencyCode).getSell());
        return amount.multiply(currencyRate);
    }

    protected boolean ifEnoughAmount(BigDecimal neededAmount, BigDecimal requestAmount) {

        return neededAmount.compareTo(requestAmount) <= 0;

    }

    @Transactional
    protected void subtractFromAccount(BigDecimal neededAmount, Account account) {
        BigDecimal newBalance = account.getBalance().subtract(neededAmount);
        accountService.updateAccount(account,newBalance);
    }

    @Transactional
    protected void addToAccount(BigDecimal amount, Account account) {
        BigDecimal newBalance = account.getBalance().add(amount);
        accountService.updateAccount(account,newBalance);
    }



}
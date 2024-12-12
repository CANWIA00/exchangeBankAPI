package com.canwia.BankExchange.service;


import com.canwia.BankExchange.dto.ExchangeDto;
import com.canwia.BankExchange.dto.converter.ExchangeDtoConverter;
import com.canwia.BankExchange.dto.requests.BuyExchangeRequest;

import com.canwia.BankExchange.exception.CustomException;
import com.canwia.BankExchange.model.Account;
import com.canwia.BankExchange.model.Currency;
import com.canwia.BankExchange.model.Exchange;
import com.canwia.BankExchange.model.Operation;
import com.canwia.BankExchange.repository.ExchangeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
public class ExchangeService {

    private final ExchangeRepository exchangeRepository;
    private final AccountService accountService;



    private final AmqpTemplate rabbitTemplate;
    private final DirectExchange directExchange;
    private final CurrencyService currencyService;
    private final ExchangeDtoConverter exchangeDtoConverter;


    @Value("${sample.rabbitmq.routingKey}")
    String routingKey;

    @Value("${sample.rabbitmq.queue}")
    String queueName;

    public ExchangeService(ExchangeRepository exchangeRepository, AccountService accountService, AmqpTemplate rabbitTemplate, DirectExchange directExchange, CurrencyService currencyService, ExchangeDtoConverter exchangeDtoConverter) {
        this.exchangeRepository = exchangeRepository;
        this.accountService = accountService;
        this.rabbitTemplate = rabbitTemplate;
        this.directExchange = directExchange;
        this.currencyService = currencyService;
        this.exchangeDtoConverter = exchangeDtoConverter;
    }

    //Directly, send to the rabbitMQ our request and our first step gonna read our message then all the process gonna start.
    public void createExchange(BuyExchangeRequest buyExchangeRequest) {
        rabbitTemplate.convertAndSend(directExchange.getName(), routingKey, buyExchangeRequest);
    }

    //*** First-Step ***
    // Get account from account service.
    // Calculate how much do we need to subtract for withdraw amount.
    // Compare if is there enough money in from_Account
    // Send it to the second_queue
    @RabbitListener(queues = "${sample.rabbitmq.queue}")
    public void fromAccountProcess_Exchange(BuyExchangeRequest buyExchangeRequest) {
        //TODO Section
        Optional<Account> accountOptional = accountService.findAccountById(UUID.fromString(buyExchangeRequest.getPlnAccount_id()));
        Optional<Currency> currency = Optional.ofNullable(currencyService.findCurrencyById(buyExchangeRequest.getCurrency_code()));
        BigDecimal neededAmount = calculateNeededAmountForExchangeBuy(buyExchangeRequest.getAmount(), buyExchangeRequest.getCurrency_code());


        if (accountOptional.isPresent() && currency.isPresent()) {
            if(ifEnoughAmount(neededAmount,accountOptional.get().getBalance())){
                subtractFromAccount(neededAmount,accountOptional.get());
                rabbitTemplate.convertAndSend(directExchange.getName(), "secondRoute", buyExchangeRequest);
            }else {
                System.out.println("Internal exception - (fromAccountProcess_BuyExchange); Not enough balance or invalid currency! " + accountOptional.get().getId());
            }
        }else {
            System.out.println("Internal exception - (fromAccountProcess_BuyExchange); Account && Currency not found!");
            //TODO error handling
        }

    }


    ///*** Second-Step ***///
    // Check is that valid to_account
    // Add money to_account and save
    // if not; charge money back to the sender account
    // Send it to the third_queue
    @RabbitListener(queues = "secondStepQueue")
    public void toAccountProcess_Exchange(BuyExchangeRequest buyExchangeRequest) {
        Optional<Account> accountOptional = accountService.findAccountById(UUID.fromString(buyExchangeRequest.getOtherAccount_id()));

        accountOptional.ifPresentOrElse( account -> {
                    addToAccount(buyExchangeRequest.getAmount(), account);
                    rabbitTemplate.convertAndSend(directExchange.getName(), "thirdRoute", buyExchangeRequest);
                },
                ()->{
                    System.out.println("Internal exception - (toAccountProcess_BuyExchange); Not enough balance! " + accountOptional.get().getId());
                    Optional<Account> from_accountOptional = accountService.findAccountById(UUID.fromString(buyExchangeRequest.getPlnAccount_id()));
                    BigDecimal neededAmount = calculateNeededAmountForExchangeBuy(buyExchangeRequest.getAmount(), buyExchangeRequest.getCurrency_code());
                    addToAccount(neededAmount, from_accountOptional.get());
                }

        );
    }

    ///*** Third-Step ***//
    // save exchange to exchange repo
    // finalize transfer and make the notification
    @RabbitListener(queues = "thirdStepQueue")
    public void finalizeExchange(BuyExchangeRequest buyExchangeRequest) {
        Optional<Account> pln_AccountOptional = accountService.findAccountById(UUID.fromString(buyExchangeRequest.getPlnAccount_id()));
        Optional<Account> other_AccountOptional = accountService.findAccountById(UUID.fromString(buyExchangeRequest.getOtherAccount_id()));
        BigDecimal currencyRate = BigDecimal.valueOf(currencyService.findCurrencyById(buyExchangeRequest.getCurrency_code()).getBuy());

        pln_AccountOptional.ifPresentOrElse(account -> {
                    System.out.println("From Account: " + account.getId()+ " new account balance: " + account.getBalance());
                },
                ()-> System.out.println("Account not found!")
        );
        other_AccountOptional.ifPresentOrElse(account -> {
                    System.out.println("From Account: " + account.getId()+ " new account balance: " + account.getBalance());
                },
                ()-> System.out.println("Account not found!")
        );


            Exchange exchange = new Exchange();
            exchange.setAccount(other_AccountOptional.get());
            exchange.setPlnAccountId(pln_AccountOptional.get().getId()); //TODO ***
            exchange.setOperation(Operation.BUY);
            exchange.setExchangeRate(currencyRate);
            exchange.setPlnAmount(calculateNeededAmountForExchangeBuy(buyExchangeRequest.getAmount(), buyExchangeRequest.getCurrency_code()));
            exchange.setOtherCurrencyAmount(buyExchangeRequest.getAmount());
            exchange.setPlnCurrency("PLN");
            exchange.setOtherCurrency(buyExchangeRequest.getCurrency_code());
            exchange.setTransactionFee(BigDecimal.ZERO);
            exchange.setExchangeDate(LocalDateTime.now());
            exchangeRepository.save(exchange);

    }




    public List<ExchangeDto> getAllExchangeByAccountId(String id) {
        return exchangeDtoConverter.convertFrom(exchangeRepository.findAllByAccountId(UUID.fromString(id)));
    }

    public ExchangeDto getExchangeById(String id) {
        return exchangeDtoConverter.convertFrom(exchangeRepository.findById(UUID.fromString(id)).orElseThrow(() -> new CustomException("Exchange not found! @@ExchangeService:v35")));
    }




    protected BigDecimal calculateNeededAmountForExchangeBuy(BigDecimal amount, String currencyCode) {

        BigDecimal currencyRate = BigDecimal.valueOf(currencyService.findCurrencyById(currencyCode).getBuy());
        return amount.multiply(currencyRate);
    }

    protected boolean ifEnoughAmount(BigDecimal neededAmount, BigDecimal requestAmount) {

        return neededAmount.compareTo(requestAmount) <= 0;
    }

    protected void subtractFromAccount(BigDecimal neededAmount, Account account) {
        BigDecimal newBalance = account.getBalance().subtract(neededAmount);
        accountService.updateAccount(account,newBalance);
    }

    protected void addToAccount(BigDecimal amount, Account account) {
        BigDecimal newBalance = account.getBalance().add(amount);
        accountService.updateAccount(account,newBalance);
    }



}
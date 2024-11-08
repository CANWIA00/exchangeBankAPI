package com.canwia.BankExchange.service;

import com.canwia.BankExchange.dto.requests.CreateExchangeRequest;
import com.canwia.BankExchange.model.Account;
import com.canwia.BankExchange.model.Currency;
import com.canwia.BankExchange.model.Exchange;
import com.canwia.BankExchange.repository.ExchangeRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
public class ExchangeService {

    private final ExchangeRepository exchangeRepository;
    private final AccountService accountService;



    private final AmqpTemplate rabbitTemplate;
    private final DirectExchange directExchange;
    private final CurrencyService currencyService;


    @Value("${sample.rabbitmq.routingKey}")
    String routingKey;

    @Value("${sample.rabbitmq.queue}")
    String queueName;

    public ExchangeService(ExchangeRepository exchangeRepository, AccountService accountService, AmqpTemplate rabbitTemplate, DirectExchange directExchange, CurrencyService currencyService) {
        this.exchangeRepository = exchangeRepository;
        this.accountService = accountService;
        this.rabbitTemplate = rabbitTemplate;
        this.directExchange = directExchange;
        this.currencyService = currencyService;
    }

    //Directly, send to the rabbitMQ our request and our first step gonna read our message then all the process gonna start.
    public void createExchange(CreateExchangeRequest createExchangeRequest) {
        rabbitTemplate.convertAndSend(directExchange.getName(), routingKey, createExchangeRequest);
    }

    //*** First-Step ***
    // Get account from account service.
    // Calculate how much do we need to subtract for withdraw amount.
    // Compare if is there enough money in from_Account
    // Send it to the second_queue
    @RabbitListener(queues = "${sample.rabbitmq.queue}")
    public void fromAccountProcess_Exchange(CreateExchangeRequest createExchangeRequest) {

        Optional<Account> accountOptional = accountService.findAccountById(UUID.fromString(createExchangeRequest.getFrom_account_id()));
        Optional<Currency> currency = Optional.ofNullable(currencyService.findCurrencyById(createExchangeRequest.getCurrency_code()));
        BigDecimal neededAmount = calculateNeededAmountForExchange(createExchangeRequest.getAmount(), createExchangeRequest.getCurrency_code());


        if (accountOptional.isPresent() && currency.isPresent()) {
            if(ifEnoughAmount(neededAmount,accountOptional.get().getBalance())){
                subtractFromAccount(neededAmount,accountOptional.get());
                rabbitTemplate.convertAndSend(directExchange.getName(), "secondRoute", createExchangeRequest);
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
    public void toAccountProcess_Exchange(CreateExchangeRequest createExchangeRequest) {
        Optional<Account> accountOptional = accountService.findAccountById(UUID.fromString(createExchangeRequest.getTo_account_id()));

        accountOptional.ifPresentOrElse( account -> {
            addToAccount(createExchangeRequest.getAmount(), account);
            rabbitTemplate.convertAndSend(directExchange.getName(), "thirdRoute", createExchangeRequest);
            },
                ()->{
                    System.out.println("Internal exception - (toAccountProcess_BuyExchange); Not enough balance! " + accountOptional.get().getId());
                    Optional<Account> from_accountOptional = accountService.findAccountById(UUID.fromString(createExchangeRequest.getFrom_account_id()));
                    BigDecimal neededAmount = calculateNeededAmountForExchange(createExchangeRequest.getAmount(), createExchangeRequest.getCurrency_code());
                    addToAccount(neededAmount, from_accountOptional.get());
                }

        );
    }

    ///*** Third-Step ***//
    // save exchange to exchange repo
    // finalize transfer and make the notification
    @RabbitListener(queues = "thirdStepQueue")
    public void finalizeExchange(CreateExchangeRequest createExchangeRequest) {
        Optional<Account> from_AccountOptional = accountService.findAccountById(UUID.fromString(createExchangeRequest.getFrom_account_id()));
        Optional<Account> to_AccountOptional = accountService.findAccountById(UUID.fromString(createExchangeRequest.getTo_account_id()));
        Exchange exchange = new Exchange();

        from_AccountOptional.ifPresentOrElse(account -> {
            System.out.println("From Account: " + account.getId()+ " new account balance: " + account.getBalance());
        },
                ()-> System.out.println("Account not found!")
        );
        to_AccountOptional.ifPresentOrElse(account -> {
                    System.out.println("From Account: " + account.getId()+ " new account balance: " + account.getBalance());
                },
                ()-> System.out.println("Account not found!")
        );

        exchange.setCurrencyCode(createExchangeRequest.getCurrency_code());
        exchange.setAmount(createExchangeRequest.getAmount());
        exchange.setExchangeDate(LocalDateTime.now());
        exchange.setAccount(from_AccountOptional.get());

        exchangeRepository.save(exchange);

    }











    protected BigDecimal calculateNeededAmountForExchange(BigDecimal buyAmount, String currencyCode) {

        BigDecimal currencyRate = BigDecimal.valueOf(currencyService.findCurrencyById(currencyCode).getBuy());
        return buyAmount.multiply(currencyRate);
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

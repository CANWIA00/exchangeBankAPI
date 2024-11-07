package com.canwia.BankExchange.service;

import org.springframework.stereotype.Service;

@Service
public class ExchangeService {
/*
    private final ExchangeRepository exchangeRepository;
    private final AccountService accountService;
    private final NbpApiService nbpApiService;


    private final AmqpTemplate rabbitTemplate;

    private final DirectExchange directExchange;
    private final CurrencyService currencyService;

    @Value("${sample.rabbitmq.routingKey}")
    String routingKey;

    @Value("${sample.rabbitmq.queue}")
    String queueName;

    public ExchangeService(ExchangeRepository exchangeRepository, AmqpTemplate amqpTemplate, AccountService accountService, NbpApiService nbpApiService, AmqpTemplate rabbitTemplate, DirectExchange directExchange, CurrencyService currencyService) {
        this.exchangeRepository = exchangeRepository;
        this.accountService = accountService;
        this.nbpApiService = nbpApiService;
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
    // Calculate how much do we need to subtract for withdraw amount. (Do it in  NbpService for SOLID)
    // Compare if is there enough money
    @RabbitListener(queues = "${sample.rabbitmq.queue}")
    public void createExchangeMessage(CreateExchangeRequest createExchangeRequest) {

        Optional<Account> accountOptional = accountService.findAccountById(createExchangeRequest.getFrom_account_id());
        Optional<Currency> currency = Optional.ofNullable(currencyService.getCurrencyByCode(createExchangeRequest.getCurrency_code()));



    }*/
}

package com.canwia.BankExchange.controller;

import com.canwia.BankExchange.dto.requests.CreateExchangeRequest;
import com.canwia.BankExchange.service.ExchangeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/exchange")
public class ExchangeController {


    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }


    //Buying Process foreign money
    @PostMapping
    public ResponseEntity<String> createExchange(@RequestBody CreateExchangeRequest createExchangeRequest) {
        exchangeService.createExchange(createExchangeRequest);

    return ResponseEntity.ok("Exchange request has been created");
    }

}

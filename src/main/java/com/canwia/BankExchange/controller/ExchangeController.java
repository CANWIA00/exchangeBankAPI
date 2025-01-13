package com.canwia.BankExchange.controller;

import com.canwia.BankExchange.dto.ExchangeDto;
import com.canwia.BankExchange.dto.requests.ExchangeRequest;
import com.canwia.BankExchange.service.ExchangeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/exchange")
public class ExchangeController {


    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }


    //Exchange Operation
    @PostMapping
    public ResponseEntity<Void> createExchange(@RequestBody ExchangeRequest exchangeRequest) {
        exchangeService.createExchange(exchangeRequest);
       return ResponseEntity.ok().build();

    }

    @GetMapping
    public ResponseEntity<List<ExchangeDto>> getAllExchangesByUserId() {
       return ResponseEntity.ok(exchangeService.getAllExchangeByUserId());
    }


    //Exchange history for account id
    @GetMapping("/account/{id}")
    public ResponseEntity<List<ExchangeDto>> getExchangeByAccountId(@PathVariable String id) {
        return ResponseEntity.ok(exchangeService.getAllExchangeByAccountId(id));
    }

    //Exchange
    @GetMapping("/{id}")
    public ResponseEntity<ExchangeDto> getExchangeById(@PathVariable String id) {
        return ResponseEntity.ok(exchangeService.getExchangeById(id));
    }


}

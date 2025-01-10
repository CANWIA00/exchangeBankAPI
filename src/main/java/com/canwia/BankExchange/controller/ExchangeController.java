package com.canwia.BankExchange.controller;

import com.canwia.BankExchange.dto.ExchangeDto;
import com.canwia.BankExchange.dto.requests.BuyExchangeRequest;
import com.canwia.BankExchange.dto.requests.SellExchangeRequest;
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


    //Buying Process foreign money
    @PostMapping
    public ResponseEntity<Void> createExchange(@RequestBody BuyExchangeRequest buyExchangeRequest) {
        exchangeService.createExchange(buyExchangeRequest);
       return ResponseEntity.ok().build();

    }

    @PostMapping("/sell")
    public ResponseEntity<String> sellExchange(@RequestBody SellExchangeRequest sellExchangeRequest) {
       // exchangeService.sellExchange(sellExchangeRequest);
        return ResponseEntity.ok("Exchange request has been created");
    }


    // Exchange history for user's all account TODO
    /*
    @GetMapping("/user/{id}")
    public ResponseEntity<List<ExchangeDto>> getAllExchangeByUserId(@PathVariable String id) {
       return ResponseEntity.ok(exchangeService.getAllExchangeByUserId(id));
    }
    */

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

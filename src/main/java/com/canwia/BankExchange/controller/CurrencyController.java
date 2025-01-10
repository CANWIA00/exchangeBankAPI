package com.canwia.BankExchange.controller;

import com.canwia.BankExchange.dto.CurrencyDto;
import com.canwia.BankExchange.model.Currency;
import com.canwia.BankExchange.service.CurrencyService;
import com.canwia.BankExchange.util.CurrencyData;
import com.canwia.BankExchange.util.TableData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class CurrencyController {


    private final CurrencyService currencyService;


    public CurrencyController( CurrencyService currencyService) {
        this.currencyService = currencyService;
    }


    @GetMapping("/currency/table")
    public ResponseEntity<List<CurrencyDto>> getAllCurrencyByTableId() {
        return ResponseEntity.ok(currencyService.getAllCurrencyByTable());
    }


    @GetMapping("/currency/id/{id}")
    public  ResponseEntity<CurrencyDto> getCurrencyById(@PathVariable String id) {
        return ResponseEntity.ok(currencyService.getCurrencyById(id));
    }

    @GetMapping("/currency/table/period/{id}")
    public ResponseEntity<CurrencyData> getCurrencyPeriodById(@PathVariable String id) {
        return ResponseEntity.ok(currencyService.getCurrencyPeriodById(id));
    }

}

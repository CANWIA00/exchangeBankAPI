package com.canwia.BankExchange.service;

import com.canwia.BankExchange.dto.CurrencyDto;
import com.canwia.BankExchange.dto.converter.CurrencyDtoConverter;
import com.canwia.BankExchange.model.Currency;
import com.canwia.BankExchange.util.TableData;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CurrencyService {

    String URL_TABLE_C = "https://api.nbp.pl/api/exchangerates/rates/c/";
    String URL_TABLE_ALL = "https://api.nbp.pl/api/exchangerates/tables/";
    private final RestTemplate restTemplate;
    private final CurrencyDtoConverter currencyDtoConverter;

    public CurrencyService(RestTemplate restTemplate, CurrencyDtoConverter currencyDtoConverter) {
        this.restTemplate = restTemplate;
        this.currencyDtoConverter = currencyDtoConverter;
    }


    public List<CurrencyDto> getAllCurrencyByTable(String id) {
        String url = URL_TABLE_ALL + id;
        TableData[] tableData = restTemplate.getForObject(url, TableData[].class);

        if (tableData != null && tableData.length > 0) {
            // Flatten the list of rates and convert them to Currency objects
            List<Currency> currencyList = Arrays.stream(tableData)
                    .flatMap(data -> data.getRates().stream())
                    .map(rate -> new Currency(rate.getCurrency(), rate.getCode(), rate.getAsk(),rate.getBid(), rate.getMid()))
                    .collect(Collectors.toList());

            return currencyDtoConverter.convertFrom(currencyList);
        } else {
            return null;
        }


    }
}

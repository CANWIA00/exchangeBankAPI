package com.canwia.BankExchange.service;

import com.canwia.BankExchange.dto.CurrencyDto;
import com.canwia.BankExchange.dto.converter.CurrencyDtoConverter;
import com.canwia.BankExchange.model.Currency;
import com.canwia.BankExchange.util.CurrencyData;
import com.canwia.BankExchange.util.TableData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
        return currencyDtoConverter.convertFrom(findAllCurrencyByTable(id));
    }

    public CurrencyDto getCurrencyById(String id) {
        return currencyDtoConverter.convertFrom(findCurrencyById(id));
    }





    protected Currency findCurrencyById(String id) {
        String url = URL_TABLE_C + id;
        CurrencyData currencyData = restTemplate.getForObject(url, CurrencyData.class);

        if(currencyData != null) {

            Currency currency = new Currency();
            currency.setCode(currencyData.getCode());
            currency.setName(currencyData.getCurrency());
            currency.setMid(currencyData.getRatesData().getFirst().getMid());
            currency.setBuy(currencyData.getRatesData().getFirst().getAsk());
            currency.setSell(currencyData.getRatesData().getFirst().getBind());

            return currency;

        }else {
            return null; //TODO error handling
        }


    }

    protected List<Currency> findAllCurrencyByTable(String id) {
        String url = URL_TABLE_ALL + id;
        TableData[] tableData = restTemplate.getForObject(url, TableData[].class);

        if (tableData != null && tableData.length > 0) {

            return Arrays.stream(tableData)
                    .flatMap(data -> data.getRates().stream())
                    .map(rate -> new Currency(rate.getCurrency(), rate.getCode(), rate.getAsk(),rate.getBid(), rate.getMid()))
                    .collect(Collectors.toList());
        } else {
            return null; //TODO error handling
        }
    }



}

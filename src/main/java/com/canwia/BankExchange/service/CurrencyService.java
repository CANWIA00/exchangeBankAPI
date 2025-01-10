package com.canwia.BankExchange.service;

import com.canwia.BankExchange.dto.CurrencyDto;
import com.canwia.BankExchange.dto.converter.CurrencyDtoConverter;
import com.canwia.BankExchange.model.Currency;
import com.canwia.BankExchange.util.CurrencyData;
import com.canwia.BankExchange.util.TableData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CurrencyService {

    String URL_TABLE_C = "https://api.nbp.pl/api/exchangerates/rates/c/";
    String URL_TABLE_ALL = "https://api.nbp.pl/api/exchangerates/tables/c";
    private final RestTemplate restTemplate;
    private final CurrencyDtoConverter currencyDtoConverter;

    public CurrencyService(RestTemplate restTemplate, CurrencyDtoConverter currencyDtoConverter) {
        this.restTemplate = restTemplate;
        this.currencyDtoConverter = currencyDtoConverter;
    }


    public List<CurrencyDto> getAllCurrencyByTable() {
        return currencyDtoConverter.convertFrom(findAllCurrencyByTable());
    }

    public CurrencyDto getCurrencyById(String id) {
        return currencyDtoConverter.convertFrom(findCurrencyById(id));
    }

    public CurrencyData getCurrencyPeriodById(String id) {
        return getCurrencyPeriodTable(id);
    }



    protected CurrencyData getCurrencyPeriodTable(String id) {
        String url = URL_TABLE_C + id + "/last/30";
        CurrencyData currency = restTemplate.getForObject(url, CurrencyData.class);

        return currency;
    }





    protected Currency findCurrencyById(String id) {
        String url = URL_TABLE_C + id;
        CurrencyData currencyData = restTemplate.getForObject(url, CurrencyData.class);

        if(currencyData != null) {

            Currency currency = new Currency();
            currency.setCode(currencyData.getCode());
            currency.setName(currencyData.getCurrency());
            currency.setBuy(currencyData.getRatesData().getFirst().getAsk());
            currency.setSell(currencyData.getRatesData().getFirst().getBind());
            currency.setDate(currencyData.getRatesData().getFirst().getEffectiveDate());

            return currency;

        }else {
            return null; //TODO error handling
        }


    }

    protected List<Currency> findAllCurrencyByTable() {
        String url = URL_TABLE_ALL;
        TableData[] tableData = restTemplate.getForObject(url, TableData[].class);
        ArrayList<String> dateTime = Arrays.stream(tableData).map(TableData::getEffectiveDate).collect(Collectors.toCollection(ArrayList::new));

        if (tableData != null && tableData.length > 0) {

            return Arrays.stream(tableData)
                    .flatMap(data -> data.getRates().stream())
                    .map(rate -> new Currency( rate.getCode(),rate.getCurrency(), rate.getAsk(),rate.getBid(), dateTime.getFirst()))
                    .collect(Collectors.toList());
        } else {
            return null; //TODO error handling
        }
    }



}

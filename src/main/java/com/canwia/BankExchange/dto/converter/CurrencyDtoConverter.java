package com.canwia.BankExchange.dto.converter;

import com.canwia.BankExchange.dto.CurrencyDto;
import com.canwia.BankExchange.model.Currency;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CurrencyDtoConverter {

    public CurrencyDto convertFrom(Currency currency){
        CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setName(currency.getName());
        currencyDto.setCode(currency.getCode());
        currencyDto.setBuy(currency.getBuy());
        currencyDto.setSell(currency.getSell());
        currencyDto.setDate(currency.getDate());
        return currencyDto;
    }

    public List<CurrencyDto> convertFrom(List<Currency> currencyList){
        return currencyList.stream().map(this::convertFrom).toList();
    }
}

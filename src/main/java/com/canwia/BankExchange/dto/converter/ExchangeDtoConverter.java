package com.canwia.BankExchange.dto.converter;

import com.canwia.BankExchange.dto.ExchangeDto;
import com.canwia.BankExchange.model.Exchange;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExchangeDtoConverter {



    public ExchangeDto convertFrom(Exchange exchange){

        ExchangeDto exchangeDto = new ExchangeDto();
        exchangeDto.setId(String.valueOf(exchange.getId()));
        exchangeDto.setOther_account_id(String.valueOf(exchange.getAccount().getId()));
        exchangeDto.setPln_account_id(exchange.getPlnAccountId().toString());
        exchangeDto.setPlnAmount((exchange.getPlnAmount().floatValue()));
        exchangeDto.setOtherAmount(exchange.getOtherCurrencyAmount().floatValue());
        exchangeDto.setPlnCurrency(exchange.getPlnCurrency());
        exchangeDto.setOtherCurrency(exchange.getOtherCurrency());
        exchangeDto.setOperation(exchange.getOperation().toString());
        exchangeDto.setCurrencyRate(exchange.getExchangeRate().floatValue());
        exchangeDto.setTransactionFee(exchange.getTransactionFee());
        exchangeDto.setExchangeDate(exchange.getExchangeDate());

        return exchangeDto;
    }


    public List<ExchangeDto> convertFrom(List<Exchange> exchanges){
        return exchanges.stream().map(this::convertFrom).collect(Collectors.toList());
    }
}

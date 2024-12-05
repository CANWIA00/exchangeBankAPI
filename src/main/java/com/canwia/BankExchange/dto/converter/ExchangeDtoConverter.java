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



        return new ExchangeDto();
    }


    public List<ExchangeDto> convertFrom(List<Exchange> exchanges){
        return exchanges.stream().map(this::convertFrom).collect(Collectors.toList());
    }
}

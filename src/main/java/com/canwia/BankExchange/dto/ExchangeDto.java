package com.canwia.BankExchange.dto;

import lombok.Data;

import java.lang.ref.PhantomReference;
import java.time.LocalDateTime;

@Data
public class ExchangeDto {

    //Account id which is bought currency
    private String from_account_id;
    //Account id which is used for buying/selling process
    private String to_account_id;

    private String currencyCode;

    private float amount;

    private LocalDateTime exchangeDate;
}

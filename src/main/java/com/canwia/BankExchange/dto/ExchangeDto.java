package com.canwia.BankExchange.dto;

import com.canwia.BankExchange.model.Account;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.lang.ref.PhantomReference;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ExchangeDto {

    private String id;
    //Account id which is bought currency
    private String from_account_id;
    //Account id which is used for buying/selling process
    private String to_account_id;

    private float fromAmount;

    private String fromCurrency;

    private float toAmount;

    private String toCurrency;



    private float currencyRate;

    private LocalDateTime exchangeDate;
}




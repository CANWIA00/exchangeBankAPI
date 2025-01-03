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
    private String pln_account_id;
    //Account id which is used for buying/selling process
    private String other_account_id;

    private float plnAmount;

    private String plnCurrency;

    private float otherAmount;

    private String otherCurrency;

    private String operation;

    private float currencyRate;

    private BigDecimal transactionFee;

    private LocalDateTime exchangeDate;
}




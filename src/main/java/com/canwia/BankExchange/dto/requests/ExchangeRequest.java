package com.canwia.BankExchange.dto.requests;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeRequest {
    private String plnAccount_id;

    private String otherAccount_id;

    private BigDecimal amount;

    private String operation;
}

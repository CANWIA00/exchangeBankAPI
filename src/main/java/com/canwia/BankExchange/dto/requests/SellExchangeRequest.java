package com.canwia.BankExchange.dto.requests;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SellExchangeRequest {

    private String plnAccount_id;

    private String otherAccount_id;

    private String currency_code;

    private BigDecimal amount;


}

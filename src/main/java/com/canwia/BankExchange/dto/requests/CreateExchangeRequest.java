package com.canwia.BankExchange.dto.requests;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateExchangeRequest {

    private String from_account_id;

    private String to_account_id;

    private String currency_code;

    private BigDecimal amount;
}

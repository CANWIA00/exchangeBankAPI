package com.canwia.BankExchange.dto.requests;

import lombok.Data;

@Data
public class CreateExchangeRequest {

    private String from_account_id;

    private String to_account_id;

    private String currency_code;

    private float amount;
}

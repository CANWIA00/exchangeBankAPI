package com.canwia.BankExchange.dto.requests;

import lombok.Data;


@Data
public class CreateAccountRequest {

    private String currency;

    private String currencySymbol;


}

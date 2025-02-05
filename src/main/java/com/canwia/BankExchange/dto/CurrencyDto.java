package com.canwia.BankExchange.dto;

import lombok.Data;

@Data
public class CurrencyDto {

    private String code;
    private String name;
    private float buy;
    private float sell;
    private String date;
}

package com.canwia.BankExchange.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class CurrencyData implements Serializable {

    @JsonProperty("table")
    private String table;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("code")
    private String code;

    @JsonProperty("rates")
    private List<RatesData> ratesData;


    /*

     "table": "C",
    "currency": "dolar amerykański",
    "code": "USD",
    "rates": [
        {
            "no": "218/C/NBP/2024",
            "effectiveDate": "2024-11-08",
            "bid": 3.9513,
            "ask": 4.0311
        }
    ]

     */
}

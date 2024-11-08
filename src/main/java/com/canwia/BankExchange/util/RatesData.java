package com.canwia.BankExchange.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class RatesData {

    @JsonProperty("no")
    private String no;
    @JsonProperty("effectiveDate")
    private String effectiveDate;
    @JsonProperty("bid")
    private float bind;
    @JsonProperty("ask")
    private float ask;
    @JsonProperty("mid")
    private float mid;





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

package com.canwia.BankExchange.util;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class RateData implements Serializable {

    @JsonProperty("currency")
    private String currency;
    @JsonProperty("code")
    private String code;
    //Average
    @JsonProperty("mid")
    private float mid;
    //Purchase - That means if u wanna sell to the bank
    @JsonProperty("bid")
    private float bid;
    //Sell - That means if you wanna buy from the bank
    @JsonProperty("ask")
    private float ask;




}

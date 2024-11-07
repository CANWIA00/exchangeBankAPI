package com.canwia.BankExchange.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;


import java.io.Serializable;
import java.util.List;

@Getter
public class TableData implements Serializable {

    @JsonProperty("table")
    private String tableName;

    @JsonProperty("no")
    private String tableNo;

    @JsonProperty("effectiveDate")
    private String effectiveDate;

    @JsonProperty("rates")
    private List<RateData> rates;


}

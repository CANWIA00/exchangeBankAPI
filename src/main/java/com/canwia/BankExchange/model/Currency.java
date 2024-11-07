package com.canwia.BankExchange.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency {


    private String code;
    private String name;
    private float buy;
    private float sell;
    private float mid;

}

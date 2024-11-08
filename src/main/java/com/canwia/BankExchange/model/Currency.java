package com.canwia.BankExchange.model;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Currency {


    private String code;
    private String name;
    private float buy;
    private float sell;
    private float mid;

}

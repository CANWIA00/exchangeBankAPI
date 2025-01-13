package com.canwia.BankExchange.model;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Currency {


    private String code;
    private String name;
    private float buy;
    private float sell;
    private String date;

}

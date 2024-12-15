package com.canwia.BankExchange.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {

    private String name;

    private String surname;

    private String email;

    private String password;

    private LocalDate creationDate;

}

package com.canwia.BankExchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@AllArgsConstructor
public class AccountDto {

    private UUID id;

    private UUID userId;

    private String currency;

    private String currencySymbol;

    private BigDecimal balance;

    private LocalDateTime createdAt;

}

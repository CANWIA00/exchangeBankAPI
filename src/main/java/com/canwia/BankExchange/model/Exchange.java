package com.canwia.BankExchange.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exchange {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String currencyCode;

    private float amount;

    private LocalDateTime exchangeDate;


    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

}

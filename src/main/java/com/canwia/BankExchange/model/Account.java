package com.canwia.BankExchange.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name = "Accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    //************* Implement Customer
    private UUID userId;

    private String currency;

    private String currencyCode;

    private BigDecimal balance;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Exchange> exchanges;


    @Override
    public String toString() {
        return "Account [id=" + id + ", userId=" + userId + ", currency=" + currency + ", currencyCode=" + currencyCode + ", balance="+ balance + ", createdAt=" + createdAt + "]";
    }
}

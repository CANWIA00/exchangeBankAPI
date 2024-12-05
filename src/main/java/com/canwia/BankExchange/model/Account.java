package com.canwia.BankExchange.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // User ID representing the owner of the account (could be a foreign key to a User entity)
    private UUID userId;

    // Currency the account is in (e.g., PLN, USD, EUR, etc.)
    private String currency;

    // Currency code (e.g., "PLN" for Polish Zloty)
    private String currencyCode;

    // Balance for the account
    private BigDecimal balance;

    // The time when the account was created
    private LocalDateTime createdAt;

    // A collection of exchanges related to this account
    @OneToMany(mappedBy = "sourceAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Exchange> sourceExchanges; // Exchanges where this account is the source (PLN Account)

    @OneToMany(mappedBy = "targetAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Exchange> targetExchanges; // Exchanges where this account is the target (Other currency account)

    @Override
    public String toString() {
        return "Account [id=" + id + ", userId=" + userId + ", currency=" + currency + ", currencyCode=" + currencyCode + ", balance=" + balance + ", createdAt=" + createdAt + "]";
    }

}

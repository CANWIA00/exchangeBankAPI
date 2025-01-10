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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Currency the account is in (e.g., PLN, USD, EUR, etc.)
    private String currency;

    // Currency code (e.g., "PLN" for Polish Zloty)
    private String currencyCode;

    // Balance for the account
    private BigDecimal balance;

    // The time when the account was created
    private LocalDateTime createdAt;


    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Exchange> exchanges;


    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", user=" + user.getId() +
                ", currency='" + currency + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", balance=" + balance +
                ", createdAt=" + createdAt +
                ", exchanges=" + exchanges +
                '}';
    }
}

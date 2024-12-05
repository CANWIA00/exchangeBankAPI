package com.canwia.BankExchange.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "exchanges")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exchange {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // Source Account (PLN Account in case of exchange)
    @ManyToOne
    @JoinColumn(name = "source_account", nullable = false)
    private Account sourceAccount;

    // Target Account (Other currency account)
    @ManyToOne
    @JoinColumn(name = "target_account", nullable = false)
    private Account targetAccount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Operation operation; // Operation: BUY or SELL

    @Column(name = "exchange_rate", precision = 10, scale = 6, nullable = false)
    private BigDecimal exchangeRate;

    @Column(name = "pln_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal plnAmount;

    @Column(name = "other_currency_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal otherCurrencyAmount;

    @Column(name = "transaction_fee", precision = 15, scale = 2, nullable = false, columnDefinition = "DECIMAL(15, 2) DEFAULT 0.00")
    private BigDecimal transactionFee = BigDecimal.ZERO;

    @Column(name = "exchange_date", nullable = false)
    private LocalDateTime exchangeDate = LocalDateTime.now();

    @Column(nullable = false, length = 20)
    private String status = "SUCCESS"; // Status of the transaction

    @Column(name = "error_message")
    private String errorMessage; // Error message in case of failure

}

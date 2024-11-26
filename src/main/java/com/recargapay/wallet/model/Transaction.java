package com.recargapay.wallet.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_wallet_id", nullable = false)
    private Wallet sourceWallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_wallet_id")
    private Wallet targetWallet;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private String description;

    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, TRANSFER
    }

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}
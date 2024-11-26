package com.recargapay.wallet.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDTO {
    private Long id;
    private Long sourceWalletId;
    private Long targetWalletId;
    private BigDecimal amount;
    private String type;
    private String timestamp;
}
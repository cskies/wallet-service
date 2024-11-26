package com.recargapay.wallet.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletResponseDTO {
    private Long id;
    private String userId;
    private BigDecimal balance;
}

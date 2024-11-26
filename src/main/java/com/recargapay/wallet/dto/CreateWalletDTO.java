package com.recargapay.wallet.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateWalletDTO {
    @NotBlank(message = "User ID is required")
    private String userId;
}
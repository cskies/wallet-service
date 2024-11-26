package com.recargapay.wallet.exception;

public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException(Long id) {
        super("Wallet not found with id: " + id);
    }
}
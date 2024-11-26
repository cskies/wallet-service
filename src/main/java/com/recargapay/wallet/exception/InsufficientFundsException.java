package com.recargapay.wallet.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException() {
        super("Insufficient funds for this operation");
    }
}

package com.recargapay.wallet.controller;

import com.recargapay.wallet.dto.*;
import com.recargapay.wallet.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<WalletResponseDTO> createWallet(@Valid @RequestBody CreateWalletDTO request) {
        return ResponseEntity.ok(walletService.createWallet(request));
    }

    @GetMapping("/{walletId}/balance")
    public ResponseEntity<BalanceDTO> getBalance(@PathVariable Long walletId) {
        return ResponseEntity.ok(walletService.getBalance(walletId));
    }

    @GetMapping("/{walletId}/balance/historical")
    public ResponseEntity<BalanceDTO> getHistoricalBalance(
            @PathVariable Long walletId,
            @RequestParam String date) {
        return ResponseEntity.ok(walletService.getHistoricalBalance(walletId, date));
    }

    @PostMapping("/{walletId}/deposit")
    public ResponseEntity<TransactionDTO> deposit(
            @PathVariable Long walletId,
            @Valid @RequestBody MoneyOperationDTO request) {
        return ResponseEntity.ok(walletService.deposit(walletId, request));
    }

    @PostMapping("/{walletId}/withdraw")
    public ResponseEntity<TransactionDTO> withdraw(
            @PathVariable Long walletId,
            @Valid @RequestBody MoneyOperationDTO request) {
        return ResponseEntity.ok(walletService.withdraw(walletId, request));
    }

    @PostMapping("/{sourceWalletId}/transfer")
    public ResponseEntity<TransactionDTO> transfer(
            @PathVariable Long sourceWalletId,
            @Valid @RequestBody TransferDTO request) {
        return ResponseEntity.ok(walletService.transfer(sourceWalletId, request));
    }
}
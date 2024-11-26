package com.recargapay.wallet.service;

import com.recargapay.wallet.dto.*;
import com.recargapay.wallet.exception.*;
import com.recargapay.wallet.model.*;
import com.recargapay.wallet.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final AuditService auditService;

    @Transactional
    public WalletResponseDTO createWallet(CreateWalletDTO request) {
        Wallet wallet = new Wallet();
        wallet.setUserId(request.getUserId());
        wallet.setBalance(BigDecimal.ZERO);

        wallet = walletRepository.save(wallet);
        auditService.logEvent("WALLET", wallet.getId(), "CREATE", wallet.getUserId(), null, wallet);

        return mapToWalletResponse(wallet);
    }

    public BalanceDTO getBalance(Long walletId) {
        Wallet wallet = findWalletOrThrow(walletId);
        return new BalanceDTO(wallet.getBalance());
    }

    public BalanceDTO getHistoricalBalance(Long walletId, String dateStr) {
        Wallet wallet = findWalletOrThrow(walletId);
        LocalDateTime date = LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_DATE_TIME);

        BigDecimal balance = calculateHistoricalBalance(wallet, date);
        return new BalanceDTO(balance);
    }

    @Transactional
    public TransactionDTO deposit(Long walletId, MoneyOperationDTO request) {
        Wallet wallet = findWalletOrThrow(walletId);
        validateAmount(request.getAmount());

        BigDecimal oldBalance = wallet.getBalance();
        wallet.setBalance(wallet.getBalance().add(request.getAmount()));
        wallet = walletRepository.save(wallet);

        Transaction transaction = createTransaction(wallet, null,
                request.getAmount(), Transaction.TransactionType.DEPOSIT);

        auditService.logEvent("WALLET", wallet.getId(), "UPDATE",
                wallet.getUserId(), oldBalance, wallet.getBalance());

        return mapToTransactionDTO(transaction);
    }

    @Transactional
    public TransactionDTO withdraw(Long walletId, MoneyOperationDTO request) {
        Wallet wallet = findWalletOrThrow(walletId);
        validateAmount(request.getAmount());
        validateSufficientFunds(wallet, request.getAmount());

        BigDecimal oldBalance = wallet.getBalance();
        wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
        wallet = walletRepository.save(wallet);

        Transaction transaction = createTransaction(wallet, null,
                request.getAmount(), Transaction.TransactionType.WITHDRAWAL);

        auditService.logEvent("WALLET", wallet.getId(), "UPDATE",
                wallet.getUserId(), oldBalance, wallet.getBalance());

        return mapToTransactionDTO(transaction);
    }

    @Transactional
    public TransactionDTO transfer(Long sourceWalletId, TransferDTO request) {
        Wallet sourceWallet = findWalletOrThrow(sourceWalletId);
        Wallet targetWallet = findWalletOrThrow(request.getTargetWalletId());

        validateAmount(request.getAmount());
        validateSufficientFunds(sourceWallet, request.getAmount());

        BigDecimal oldSourceBalance = sourceWallet.getBalance();
        BigDecimal oldTargetBalance = targetWallet.getBalance();

        sourceWallet.setBalance(sourceWallet.getBalance().subtract(request.getAmount()));
        targetWallet.setBalance(targetWallet.getBalance().add(request.getAmount()));

        walletRepository.save(sourceWallet);
        walletRepository.save(targetWallet);

        Transaction transaction = createTransaction(sourceWallet, targetWallet,
                request.getAmount(), Transaction.TransactionType.TRANSFER);

        auditService.logEvent("WALLET", sourceWallet.getId(), "UPDATE",
                sourceWallet.getUserId(), oldSourceBalance, sourceWallet.getBalance());
        auditService.logEvent("WALLET", targetWallet.getId(), "UPDATE",
                targetWallet.getUserId(), oldTargetBalance, targetWallet.getBalance());

        return mapToTransactionDTO(transaction);
    }

    private Wallet findWalletOrThrow(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId));
    }

    private void validateAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException();
        }
    }

    private void validateSufficientFunds(Wallet wallet, BigDecimal amount) {
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }
    }

    private Transaction createTransaction(Wallet sourceWallet, Wallet targetWallet,
                                          BigDecimal amount, Transaction.TransactionType type) {
        Transaction transaction = new Transaction();
        transaction.setSourceWallet(sourceWallet);
        transaction.setTargetWallet(targetWallet);
        transaction.setAmount(amount);
        transaction.setType(type);

        return transactionRepository.save(transaction);
    }

    private BigDecimal calculateHistoricalBalance(Wallet wallet, LocalDateTime date) {
        List<Transaction> transactions = transactionRepository
                .findTransactionsBeforeDate(wallet, date);

        return calculateBalanceFromTransactions(wallet, transactions);
    }

    private TransactionDTO mapToTransactionDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setSourceWalletId(transaction.getSourceWallet().getId());
        dto.setTargetWalletId(transaction.getTargetWallet() != null ?
                transaction.getTargetWallet().getId() : null);
        dto.setAmount(transaction.getAmount());
        dto.setType(transaction.getType().name());
        dto.setTimestamp(transaction.getTimestamp().toString());
        return dto;
    }

    private WalletResponseDTO mapToWalletResponse(Wallet wallet) {
        WalletResponseDTO dto = new WalletResponseDTO();
        dto.setId(wallet.getId());
        dto.setUserId(wallet.getUserId());
        dto.setBalance(wallet.getBalance());
        return dto;
    }

    private BigDecimal calculateBalanceFromTransactions(Wallet wallet, List<Transaction> transactions) {
        BigDecimal balance = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {
            if (transaction.getSourceWallet().getId().equals(wallet.getId())) {
                if (transaction.getType() == Transaction.TransactionType.WITHDRAWAL ||
                        transaction.getType() == Transaction.TransactionType.TRANSFER) {
                    balance = balance.subtract(transaction.getAmount());
                }
            }

            if (transaction.getTargetWallet() != null &&
                    transaction.getTargetWallet().getId().equals(wallet.getId())) {
                if (transaction.getType() == Transaction.TransactionType.DEPOSIT ||
                        transaction.getType() == Transaction.TransactionType.TRANSFER) {
                    balance = balance.add(transaction.getAmount());
                }
            }
        }

        return balance;

    }
}
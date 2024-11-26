package com.recargapay.wallet.repository;

import com.recargapay.wallet.model.Transaction;
import com.recargapay.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySourceWalletOrderByTimestampDesc(Wallet wallet);

    @Query("SELECT t FROM Transaction t WHERE (t.sourceWallet = :wallet OR t.targetWallet = :wallet) " +
            "AND t.timestamp <= :dateTime ORDER BY t.timestamp DESC")
    List<Transaction> findTransactionsBeforeDate(Wallet wallet, LocalDateTime dateTime);
}
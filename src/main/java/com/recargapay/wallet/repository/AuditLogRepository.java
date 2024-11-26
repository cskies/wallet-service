package com.recargapay.wallet.repository;

import com.recargapay.wallet.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByEntityTypeAndEntityId(String entityType, Long entityId);
    List<AuditLog> findByUserIdAndTimestampBetween(String userId, LocalDateTime startDate, LocalDateTime endDate);
    List<AuditLog> findByEntityTypeAndTimestampBetween(String entityType, LocalDateTime startDate, LocalDateTime endDate);
}
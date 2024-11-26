package com.recargapay.wallet.service;

import com.recargapay.wallet.model.AuditLog;
import com.recargapay.wallet.repository.AuditLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {
    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logEvent(String entityType, Long entityId, String action,
                         String userId, Object oldValue, Object newValue) {
        try {
            String oldValueJson = convertToJson(oldValue);
            String newValueJson = convertToJson(newValue);

            AuditLog auditLog = AuditLog.builder()
                    .entityType(entityType)
                    .entityId(entityId)
                    .action(action)
                    .userId(userId)
                    .oldValue(oldValueJson)
                    .newValue(newValueJson)
                    .ipAddress(getClientIpAddress())
                    .timestamp(LocalDateTime.now())
                    .build();

            auditLogRepository.save(auditLog);

        } catch (Exception e) {
            log.error("Failed to create audit log for entity type: {} and id: {}",
                    entityType, entityId, e);
        }
    }

    public List<AuditLog> getAuditLogsForEntity(String entityType, Long entityId) {
        return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId);
    }

    public List<AuditLog> getAuditLogsForUser(String userId,
                                              LocalDateTime startDate,
                                              LocalDateTime endDate) {
        return auditLogRepository.findByUserIdAndTimestampBetween(
                userId, startDate, endDate);
    }

    public List<AuditLog> getAuditLogsByType(String entityType,
                                             LocalDateTime startDate,
                                             LocalDateTime endDate) {
        return auditLogRepository.findByEntityTypeAndTimestampBetween(
                entityType, startDate, endDate);
    }

    private String convertToJson(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            log.error("Failed to convert object to JSON", e);
            return "Error converting to JSON";
        }
    }

    private String getClientIpAddress() {
        try {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String forwardedFor = request.getHeader("X-Forwarded-For");

                if (forwardedFor != null && !forwardedFor.isEmpty()) {
                    // Get first IP if multiple are present
                    return forwardedFor.split(",")[0].trim();
                }
                return request.getRemoteAddr();
            }
        } catch (Exception e) {
            log.error("Error getting client IP address", e);
        }
        return "unknown";
    }
}
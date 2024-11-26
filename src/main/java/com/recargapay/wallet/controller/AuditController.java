package com.recargapay.wallet.controller;

import com.recargapay.wallet.model.AuditLog;
import com.recargapay.wallet.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
public class AuditController {
    private final AuditService auditService;

    @GetMapping("/entity/{entityType}/{entityId}")
    public ResponseEntity<List<AuditLog>> getAuditLogsForEntity(
            @PathVariable String entityType,
            @PathVariable Long entityId) {
        return ResponseEntity.ok(auditService.getAuditLogsForEntity(entityType, entityId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AuditLog>> getAuditLogsForUser(
            @PathVariable String userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(auditService.getAuditLogsForUser(userId, startDate, endDate));
    }

    @GetMapping("/type/{entityType}")
    public ResponseEntity<List<AuditLog>> getAuditLogsByType(
            @PathVariable String entityType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(auditService.getAuditLogsByType(entityType, startDate, endDate));
    }
}
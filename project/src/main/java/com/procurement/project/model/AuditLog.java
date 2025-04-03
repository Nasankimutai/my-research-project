package com.procurement.project.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // For example, the username or user ID who performed the action
    private String user;

    // What action was performed (CREATE, UPDATE, DELETE, etc.)
    private String action;

    // Details about the action (could be a JSON string or plain text)
    @Column(length = 1000)
    private String details;

    // Timestamp of the action
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}

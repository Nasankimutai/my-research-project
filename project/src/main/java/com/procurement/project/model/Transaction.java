package com.procurement.project.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;
    private String supplier;
    private Double amount;
    private LocalDateTime transactionDate;
    private String status;

    @Column(length = 500)
    private String description;

    private String category;
    private String itemName;
    private String paymentMethod;
    private String department;
    private String supplierCountry;
    private String contractId;
    private String unitPrice;
    private Integer quantity;
    private String totalPrice;
    private String approvedBy;

    private Double riskScore;
    private Boolean anomalyFlag; // Our main boolean flag

    // Additional fields for "type" and "severity"
    private String anomalyType;
    private String anomalySeverity;

    private Double anomalyScore;

    private String reviewedBy;

    @Column(length = 500)
    private String reviewNotes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

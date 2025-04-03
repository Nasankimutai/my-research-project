package com.procurement.project.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    private Long id;
    private String transactionId;
    private String supplier;
    private Double amount;
    private LocalDateTime transactionDate;
    private String status;
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
    private Boolean anomalyFlag;

    // New fields
    private String anomalyType;
    private String anomalySeverity;
    private Double anomalyScore;

    private String reviewedBy;
    private String reviewNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

package com.procurement.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private Long totalTransactions;
    private Long flaggedAnomalies;
    private Long resolvedAnomalies;
    private Long pendingReviewAnomalies;
    private Double averageRiskScore;
    // Example: Monthly transaction counts, can be used for time-series charts
    private List<MonthlyDataDTO> monthlyData;
}


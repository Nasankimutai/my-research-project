package com.procurement.project.service;

import com.procurement.project.dto.DashboardDTO;
import com.procurement.project.dto.MonthlyDataDTO;
import com.procurement.project.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private TransactionRepository transactionRepository;

    //Aggregate the dashboard metrics
    public DashboardDTO getDashboardMetrics(){
        // Total transactions count
        Long totalTransactions = transactionRepository.count();

        //Total flagged anomalies
        Long flaggedAnomalies = (long) transactionRepository.findByAnomalyFlagTrue().size();

        // For this example, assume resolved anomalies are transactions with status "Resolved"
        Long resolvedAnomalies = transactionRepository.findAll()
                .stream()
                .filter(transaction -> transaction.getStatus().equals("Resolved"))
                .count();

        // pending review anomalies are flagged anomalies that are not yet resolved
        Long pendingReviewAnomalies = flaggedAnomalies - resolvedAnomalies;

        //Average risk score for flagged anomalies (if any)
        Double averageRiskScore = transactionRepository.findByAnomalyFlagTrue()
                .stream()
                .mapToDouble(t -> t.getRiskScore() != null ? t.getRiskScore() : 0)
                .average()
                .orElse(0);

        // Example: Generate monthly data (this requires that your Transaction entity has a transactionDate)
        List<MonthlyDataDTO> monthlyData = transactionRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(t -> t.getTransactionDate().toLocalDate().withDayOfMonth(1)))
                .entrySet()
                .stream()
                .map(entry -> new MonthlyDataDTO(
                        entry.getKey().toString(),
                        (long) entry.getValue().size(),
                        entry.getValue().stream().filter(t -> Boolean.TRUE.equals(t.getAnomalyFlag())).count()

                ))
                .collect(Collectors.toList());

        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setTotalTransactions(totalTransactions);
        dashboardDTO.setFlaggedAnomalies(flaggedAnomalies);
        dashboardDTO.setResolvedAnomalies(resolvedAnomalies);
        dashboardDTO.setPendingReviewAnomalies(pendingReviewAnomalies);
        dashboardDTO.setAverageRiskScore(averageRiskScore);
        dashboardDTO.setMonthlyData(monthlyData);

        return dashboardDTO;
    }
}

package com.procurement.project.service;

import com.procurement.project.dto.ReportDTO;
import com.procurement.project.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportingService {

    @Autowired
    private TransactionRepository transactionRepository;

    // Generate an anomaly report (This can be expanded to include filtering, formatting, etc.)
    public ReportDTO generateAnomalyReport(){
        // This is a dummy report can be expanded.
        List<String> details = new ArrayList<>();
        details.add("Total Transactions: " + transactionRepository.count());
        details.add("Flagged Anomalies: " + transactionRepository.findByAnomalyFlagTrue().size());
        // Additional report details can be added here

        return new ReportDTO(LocalDateTime.now(), "Anomaly Report", details);

    }
}

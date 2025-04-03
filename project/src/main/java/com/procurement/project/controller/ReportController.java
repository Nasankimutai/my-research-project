package com.procurement.project.controller;


import com.procurement.project.dto.ReportDTO;
import com.procurement.project.service.ReportingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ReportingService reportingService;

    // Endpoint to generate anomaly report
    @GetMapping("/anomalies")
    public ResponseEntity<ReportDTO> getAnomaliesReport(){
        ReportDTO report = reportingService.generateAnomalyReport();
        return ResponseEntity.ok(report);
    }
}

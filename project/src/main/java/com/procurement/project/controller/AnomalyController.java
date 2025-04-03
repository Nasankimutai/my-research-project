package com.procurement.project.controller;

import com.procurement.project.dto.TransactionDTO;
import com.procurement.project.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/anomaly")
public class AnomalyController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getFlaggedAnomalies(){
        List<TransactionDTO> anomalies = transactionService.getAnomalies();
        return ResponseEntity.ok(anomalies);
    }

    // If you want an update method
    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> updateAnomaly(
            @PathVariable Long id,
            @RequestBody TransactionDTO anomalyUpdateDTO
    ){
        TransactionDTO updatedAnomaly = transactionService.updateTransaction(id, anomalyUpdateDTO);
        return ResponseEntity.ok(updatedAnomaly);
    }
}

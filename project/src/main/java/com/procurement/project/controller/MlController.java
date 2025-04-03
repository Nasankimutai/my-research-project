package com.procurement.project.controller;

import com.procurement.project.dto.MlFeatureDTO;
import com.procurement.project.dto.MlResponseDTO;
import com.procurement.project.service.MlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ml")
public class MlController {

    @Autowired
    private MlService mlService;

    /**
     * Optional debugging or direct testing endpoint.
     * Accepts a single MlFeatureDTO, calls the ML service, returns MlResponseDTO.
     *
     * In real usage, you might not even need this controller if
     * the feature engineering is done in TransactionService
     * and calls ML directly without exposing a separate ML endpoint.
     */
    @PostMapping("/analyze")
    public ResponseEntity<MlResponseDTO> analyzeFeatures(@RequestBody MlFeatureDTO featureDTO) {
        // If your mlService method expects a single featureDTO, we can call it directly.
        // If your mlService is set to handle multiple, you can adapt as needed.
        MlResponseDTO responseDTO = mlService.analyzeFeatures(featureDTO);
        return ResponseEntity.ok(responseDTO);
    }
}

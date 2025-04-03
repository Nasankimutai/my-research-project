package com.procurement.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MlResponseDTO {
    private String transaction_id;
    private int label;           // 0 or 1
    private String anomaly_type;         // e.g. "Late Hours"
    private String severity;     // e.g. "High", "Medium", "Low"
    private double score;        // numeric score
}

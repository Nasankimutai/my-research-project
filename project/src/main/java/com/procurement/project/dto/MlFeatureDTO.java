package com.procurement.project.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents the final set of features required by the ML model.
 * Includes 'transaction_id' so we can associate the prediction
 * with a specific transaction in the database.
 */
@Getter
@Setter
public class MlFeatureDTO {

    private String transaction_id;        // so we know which transaction
    private Double hour;                 // extracted from transactionDate
    private Double after_hours_flag;     // 0 or 1
    private Double budget_deviation;     // amount - budget
    private Double round_number_flag;    // 0 or 1
    private Double duplicate_flag;       // 0 or 1
    private Double supplier_change_count; // how many distinct suppliers for user
    private Double unexpected_supplier_flag;  // 0 or 1
    private Double supplier_frequency;       // how often this supplier is used
    private Double vendor_collusion_risk;     // 0 or 1
    private Double budget_dev_freq;           // budget_deviation * supplier_frequency
}

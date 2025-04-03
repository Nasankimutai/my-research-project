package com.procurement.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyDataDTO {
    private String month;
    private Long transactionCount;
    private Long anomalyCount;
}

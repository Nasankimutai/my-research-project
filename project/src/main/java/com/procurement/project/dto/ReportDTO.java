package com.procurement.project.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private LocalDateTime reportGeneratedAt;
    private String reportType; // eg "Anomaly Report", "Audit Log"
    private List<String> details; // You later expand this to a more structured format

}

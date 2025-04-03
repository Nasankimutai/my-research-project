package com.procurement.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogDTO {
    private  Long id;
    private String action;
    private String performedBy;
    private LocalDateTime timestamp;
    private String description;
}

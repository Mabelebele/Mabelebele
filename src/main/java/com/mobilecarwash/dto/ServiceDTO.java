package com.mobilecarwash.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private Integer estimatedDuration;
    private String category;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

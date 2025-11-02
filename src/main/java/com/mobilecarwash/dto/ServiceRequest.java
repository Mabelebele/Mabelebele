package com.mobilecarwash.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequest {
    
    @NotBlank(message = "Service name is required")
    private String name;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotNull(message = "Base price is required")
    @Positive(message = "Base price must be positive")
    private BigDecimal basePrice;
    
    @NotNull(message = "Estimated duration is required")
    @Positive(message = "Estimated duration must be positive")
    private Integer estimatedDuration;
    
    @NotBlank(message = "Category is required")
    private String category;
}

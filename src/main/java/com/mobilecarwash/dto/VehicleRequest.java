package com.mobilecarwash.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRequest {
    
    @NotBlank(message = "Make is required")
    private String make;
    
    @NotBlank(message = "Model is required")
    private String model;
    
    @NotNull(message = "Year is required")
    private Integer year;
    
    @NotBlank(message = "Color is required")
    private String color;
    
    @NotBlank(message = "License plate is required")
    private String licensePlate;
    
    @NotBlank(message = "Vehicle type is required")
    private String vehicleType;
}

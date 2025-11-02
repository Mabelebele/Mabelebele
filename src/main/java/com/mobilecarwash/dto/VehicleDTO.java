package com.mobilecarwash.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {
    private Long id;
    private Long userId;
    private String make;
    private String model;
    private Integer year;
    private String color;
    private String licensePlate;
    private String vehicleType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

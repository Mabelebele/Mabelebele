package com.mobilecarwash.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    
    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;
    
    @NotNull(message = "Service ID is required")
    private Long serviceId;
    
    @NotNull(message = "Scheduled date and time is required")
    private LocalDateTime scheduledDateTime;
    
    @NotBlank(message = "Address is required")
    private String address;
    
    private String city;
    
    private String postalCode;
    
    private Double latitude;
    
    private Double longitude;
    
    private String specialInstructions;
}

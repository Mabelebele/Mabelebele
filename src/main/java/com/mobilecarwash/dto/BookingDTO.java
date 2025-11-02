package com.mobilecarwash.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Long id;
    private Long customerId;
    private String customerName;
    private Long serviceProviderId;
    private String serviceProviderName;
    private Long vehicleId;
    private String vehicleInfo;
    private Long serviceId;
    private String serviceName;
    private LocalDateTime scheduledDateTime;
    private String address;
    private String city;
    private String postalCode;
    private Double latitude;
    private Double longitude;
    private String status;
    private BigDecimal totalPrice;
    private String specialInstructions;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

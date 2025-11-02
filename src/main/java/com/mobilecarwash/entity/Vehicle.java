package com.mobilecarwash.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Vehicle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String make;
    
    @Column(nullable = false)
    private String model;
    
    @Column(nullable = false)
    private Integer year;
    
    @Column(nullable = false)
    private String color;
    
    @Column(nullable = false, unique = true)
    private String licensePlate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType vehicleType;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    public enum VehicleType {
        SEDAN,
        SUV,
        TRUCK,
        VAN,
        COUPE,
        HATCHBACK
    }
}

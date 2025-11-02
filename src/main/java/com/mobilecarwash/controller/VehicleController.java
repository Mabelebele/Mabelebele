package com.mobilecarwash.controller;

import com.mobilecarwash.dto.VehicleDTO;
import com.mobilecarwash.dto.VehicleRequest;
import com.mobilecarwash.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Vehicles", description = "Vehicle management endpoints")
public class VehicleController {
    
    private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);
    
    @Autowired
    private VehicleService vehicleService;
    
    @PostMapping
    @Operation(summary = "Create a new vehicle")
    public ResponseEntity<VehicleDTO> createVehicle(@Valid @RequestBody VehicleRequest request) {
        logger.info("Creating new vehicle");
        VehicleDTO vehicle = vehicleService.createVehicle(request);
        return new ResponseEntity<>(vehicle, HttpStatus.CREATED);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all vehicles (Admin only)")
    public ResponseEntity<List<VehicleDTO>> getAllVehicles() {
        logger.info("Fetching all vehicles");
        List<VehicleDTO> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get vehicle by ID")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable Long id) {
        logger.info("Fetching vehicle with ID: {}", id);
        VehicleDTO vehicle = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(vehicle);
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get vehicles by user ID")
    public ResponseEntity<List<VehicleDTO>> getVehiclesByUserId(@PathVariable Long userId) {
        logger.info("Fetching vehicles for user ID: {}", userId);
        List<VehicleDTO> vehicles = vehicleService.getVehiclesByUserId(userId);
        return ResponseEntity.ok(vehicles);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update vehicle")
    public ResponseEntity<VehicleDTO> updateVehicle(@PathVariable Long id, @Valid @RequestBody VehicleRequest request) {
        logger.info("Updating vehicle with ID: {}", id);
        VehicleDTO vehicle = vehicleService.updateVehicle(id, request);
        return ResponseEntity.ok(vehicle);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete vehicle")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        logger.info("Deleting vehicle with ID: {}", id);
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
}

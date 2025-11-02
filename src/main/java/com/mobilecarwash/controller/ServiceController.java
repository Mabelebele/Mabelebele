package com.mobilecarwash.controller;

import com.mobilecarwash.dto.ServiceDTO;
import com.mobilecarwash.dto.ServiceRequest;
import com.mobilecarwash.entity.Service;
import com.mobilecarwash.service.CarWashService;
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
@RequestMapping("/services")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Services", description = "Car wash service management endpoints")
public class ServiceController {
    
    private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);
    
    @Autowired
    private CarWashService carWashService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new service (Admin only)")
    public ResponseEntity<ServiceDTO> createService(@Valid @RequestBody ServiceRequest request) {
        logger.info("Creating new service");
        ServiceDTO service = carWashService.createService(request);
        return new ResponseEntity<>(service, HttpStatus.CREATED);
    }
    
    @GetMapping
    @Operation(summary = "Get all services")
    public ResponseEntity<List<ServiceDTO>> getAllServices() {
        logger.info("Fetching all services");
        List<ServiceDTO> services = carWashService.getAllServices();
        return ResponseEntity.ok(services);
    }
    
    @GetMapping("/active")
    @Operation(summary = "Get all active services")
    public ResponseEntity<List<ServiceDTO>> getActiveServices() {
        logger.info("Fetching active services");
        List<ServiceDTO> services = carWashService.getActiveServices();
        return ResponseEntity.ok(services);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get service by ID")
    public ResponseEntity<ServiceDTO> getServiceById(@PathVariable Long id) {
        logger.info("Fetching service with ID: {}", id);
        ServiceDTO service = carWashService.getServiceById(id);
        return ResponseEntity.ok(service);
    }
    
    @GetMapping("/category/{category}")
    @Operation(summary = "Get services by category")
    public ResponseEntity<List<ServiceDTO>> getServicesByCategory(@PathVariable String category) {
        logger.info("Fetching services by category: {}", category);
        List<ServiceDTO> services = carWashService.getServicesByCategory(Service.ServiceCategory.valueOf(category.toUpperCase()));
        return ResponseEntity.ok(services);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update service (Admin only)")
    public ResponseEntity<ServiceDTO> updateService(@PathVariable Long id, @Valid @RequestBody ServiceRequest request) {
        logger.info("Updating service with ID: {}", id);
        ServiceDTO service = carWashService.updateService(id, request);
        return ResponseEntity.ok(service);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete service (Admin only)")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        logger.info("Deleting service with ID: {}", id);
        carWashService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}

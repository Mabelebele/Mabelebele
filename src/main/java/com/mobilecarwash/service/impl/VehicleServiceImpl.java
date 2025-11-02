package com.mobilecarwash.service.impl;

import com.mobilecarwash.dto.VehicleDTO;
import com.mobilecarwash.dto.VehicleRequest;
import com.mobilecarwash.entity.User;
import com.mobilecarwash.entity.Vehicle;
import com.mobilecarwash.exception.BadRequestException;
import com.mobilecarwash.exception.ResourceNotFoundException;
import com.mobilecarwash.repository.VehicleRepository;
import com.mobilecarwash.service.UserService;
import com.mobilecarwash.service.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {
    
    private static final Logger logger = LoggerFactory.getLogger(VehicleServiceImpl.class);
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private UserService userService;
    
    @Override
    public VehicleDTO createVehicle(VehicleRequest request) {
        logger.info("Creating new vehicle with license plate: {}", request.getLicensePlate());
        
        if (vehicleRepository.existsByLicensePlate(request.getLicensePlate())) {
            throw new BadRequestException("Vehicle with this license plate already exists");
        }
        
        User currentUser = userService.getCurrentUser();
        
        Vehicle vehicle = new Vehicle();
        vehicle.setUser(currentUser);
        vehicle.setMake(request.getMake());
        vehicle.setModel(request.getModel());
        vehicle.setYear(request.getYear());
        vehicle.setColor(request.getColor());
        vehicle.setLicensePlate(request.getLicensePlate());
        vehicle.setVehicleType(Vehicle.VehicleType.valueOf(request.getVehicleType().toUpperCase()));
        
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        logger.info("Vehicle created successfully with ID: {}", savedVehicle.getId());
        
        return convertToDTO(savedVehicle);
    }
    
    @Override
    public VehicleDTO getVehicleById(Long id) {
        logger.info("Fetching vehicle with ID: {}", id);
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + id));
        return convertToDTO(vehicle);
    }
    
    @Override
    public List<VehicleDTO> getAllVehicles() {
        logger.info("Fetching all vehicles");
        return vehicleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<VehicleDTO> getVehiclesByUserId(Long userId) {
        logger.info("Fetching vehicles for user ID: {}", userId);
        return vehicleRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public VehicleDTO updateVehicle(Long id, VehicleRequest request) {
        logger.info("Updating vehicle with ID: {}", id);
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + id));
        
        if (!vehicle.getLicensePlate().equals(request.getLicensePlate()) &&
                vehicleRepository.existsByLicensePlate(request.getLicensePlate())) {
            throw new BadRequestException("Vehicle with this license plate already exists");
        }
        
        vehicle.setMake(request.getMake());
        vehicle.setModel(request.getModel());
        vehicle.setYear(request.getYear());
        vehicle.setColor(request.getColor());
        vehicle.setLicensePlate(request.getLicensePlate());
        vehicle.setVehicleType(Vehicle.VehicleType.valueOf(request.getVehicleType().toUpperCase()));
        
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        logger.info("Vehicle updated successfully with ID: {}", updatedVehicle.getId());
        
        return convertToDTO(updatedVehicle);
    }
    
    @Override
    public void deleteVehicle(Long id) {
        logger.info("Deleting vehicle with ID: {}", id);
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + id));
        vehicleRepository.delete(vehicle);
        logger.info("Vehicle deleted successfully with ID: {}", id);
    }
    
    private VehicleDTO convertToDTO(Vehicle vehicle) {
        VehicleDTO dto = new VehicleDTO();
        dto.setId(vehicle.getId());
        dto.setUserId(vehicle.getUser().getId());
        dto.setMake(vehicle.getMake());
        dto.setModel(vehicle.getModel());
        dto.setYear(vehicle.getYear());
        dto.setColor(vehicle.getColor());
        dto.setLicensePlate(vehicle.getLicensePlate());
        dto.setVehicleType(vehicle.getVehicleType().name());
        dto.setCreatedAt(vehicle.getCreatedAt());
        dto.setUpdatedAt(vehicle.getUpdatedAt());
        return dto;
    }
}

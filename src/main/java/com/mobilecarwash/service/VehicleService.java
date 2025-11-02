package com.mobilecarwash.service;

import com.mobilecarwash.dto.VehicleDTO;
import com.mobilecarwash.dto.VehicleRequest;

import java.util.List;

public interface VehicleService {
    VehicleDTO createVehicle(VehicleRequest request);
    
    VehicleDTO getVehicleById(Long id);
    
    List<VehicleDTO> getAllVehicles();
    
    List<VehicleDTO> getVehiclesByUserId(Long userId);
    
    VehicleDTO updateVehicle(Long id, VehicleRequest request);
    
    void deleteVehicle(Long id);
}

package com.mobilecarwash.service;

import com.mobilecarwash.dto.ServiceDTO;
import com.mobilecarwash.dto.ServiceRequest;
import com.mobilecarwash.entity.Service;

import java.util.List;

public interface CarWashService {
    ServiceDTO createService(ServiceRequest request);
    
    ServiceDTO getServiceById(Long id);
    
    List<ServiceDTO> getAllServices();
    
    List<ServiceDTO> getActiveServices();
    
    List<ServiceDTO> getServicesByCategory(Service.ServiceCategory category);
    
    ServiceDTO updateService(Long id, ServiceRequest request);
    
    void deleteService(Long id);
}

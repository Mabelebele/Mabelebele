package com.mobilecarwash.service.impl;

import com.mobilecarwash.dto.ServiceDTO;
import com.mobilecarwash.dto.ServiceRequest;
import com.mobilecarwash.entity.Service;
import com.mobilecarwash.exception.ResourceNotFoundException;
import com.mobilecarwash.repository.ServiceRepository;
import com.mobilecarwash.service.CarWashService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@Transactional
public class CarWashServiceImpl implements CarWashService {
    
    private static final Logger logger = LoggerFactory.getLogger(CarWashServiceImpl.class);
    
    @Autowired
    private ServiceRepository serviceRepository;
    
    @Override
    public ServiceDTO createService(ServiceRequest request) {
        logger.info("Creating new service: {}", request.getName());
        
        Service service = new Service();
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setBasePrice(request.getBasePrice());
        service.setEstimatedDuration(request.getEstimatedDuration());
        service.setCategory(Service.ServiceCategory.valueOf(request.getCategory().toUpperCase()));
        service.setActive(true);
        
        Service savedService = serviceRepository.save(service);
        logger.info("Service created successfully with ID: {}", savedService.getId());
        
        return convertToDTO(savedService);
    }
    
    @Override
    public ServiceDTO getServiceById(Long id) {
        logger.info("Fetching service with ID: {}", id);
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with ID: " + id));
        return convertToDTO(service);
    }
    
    @Override
    public List<ServiceDTO> getAllServices() {
        logger.info("Fetching all services");
        return serviceRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ServiceDTO> getActiveServices() {
        logger.info("Fetching active services");
        return serviceRepository.findByActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ServiceDTO> getServicesByCategory(Service.ServiceCategory category) {
        logger.info("Fetching services by category: {}", category);
        return serviceRepository.findByActiveTrueAndCategory(category).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public ServiceDTO updateService(Long id, ServiceRequest request) {
        logger.info("Updating service with ID: {}", id);
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with ID: " + id));
        
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setBasePrice(request.getBasePrice());
        service.setEstimatedDuration(request.getEstimatedDuration());
        service.setCategory(Service.ServiceCategory.valueOf(request.getCategory().toUpperCase()));
        
        Service updatedService = serviceRepository.save(service);
        logger.info("Service updated successfully with ID: {}", updatedService.getId());
        
        return convertToDTO(updatedService);
    }
    
    @Override
    public void deleteService(Long id) {
        logger.info("Deleting service with ID: {}", id);
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with ID: " + id));
        service.setActive(false);
        serviceRepository.save(service);
        logger.info("Service deactivated successfully with ID: {}", id);
    }
    
    private ServiceDTO convertToDTO(Service service) {
        ServiceDTO dto = new ServiceDTO();
        dto.setId(service.getId());
        dto.setName(service.getName());
        dto.setDescription(service.getDescription());
        dto.setBasePrice(service.getBasePrice());
        dto.setEstimatedDuration(service.getEstimatedDuration());
        dto.setCategory(service.getCategory().name());
        dto.setActive(service.getActive());
        dto.setCreatedAt(service.getCreatedAt());
        dto.setUpdatedAt(service.getUpdatedAt());
        return dto;
    }
}

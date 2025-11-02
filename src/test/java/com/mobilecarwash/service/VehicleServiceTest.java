package com.mobilecarwash.service;

import com.mobilecarwash.dto.VehicleDTO;
import com.mobilecarwash.dto.VehicleRequest;
import com.mobilecarwash.entity.User;
import com.mobilecarwash.entity.Vehicle;
import com.mobilecarwash.exception.BadRequestException;
import com.mobilecarwash.exception.ResourceNotFoundException;
import com.mobilecarwash.repository.VehicleRepository;
import com.mobilecarwash.service.impl.VehicleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {
    
    @Mock
    private VehicleRepository vehicleRepository;
    
    @Mock
    private UserService userService;
    
    @InjectMocks
    private VehicleServiceImpl vehicleService;
    
    private Vehicle testVehicle;
    private VehicleRequest vehicleRequest;
    private User testUser;
    
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        
        testVehicle = new Vehicle();
        testVehicle.setId(1L);
        testVehicle.setUser(testUser);
        testVehicle.setMake("Toyota");
        testVehicle.setModel("Camry");
        testVehicle.setYear(2022);
        testVehicle.setColor("Black");
        testVehicle.setLicensePlate("ABC123");
        testVehicle.setVehicleType(Vehicle.VehicleType.SEDAN);
        testVehicle.setCreatedAt(LocalDateTime.now());
        testVehicle.setUpdatedAt(LocalDateTime.now());
        
        vehicleRequest = new VehicleRequest();
        vehicleRequest.setMake("Toyota");
        vehicleRequest.setModel("Camry");
        vehicleRequest.setYear(2022);
        vehicleRequest.setColor("Black");
        vehicleRequest.setLicensePlate("ABC123");
        vehicleRequest.setVehicleType("SEDAN");
    }
    
    @Test
    void testCreateVehicle_Success() {
        when(vehicleRepository.existsByLicensePlate(anyString())).thenReturn(false);
        when(userService.getCurrentUser()).thenReturn(testUser);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(testVehicle);
        
        VehicleDTO result = vehicleService.createVehicle(vehicleRequest);
        
        assertNotNull(result);
        assertEquals(testVehicle.getMake(), result.getMake());
        assertEquals(testVehicle.getModel(), result.getModel());
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }
    
    @Test
    void testCreateVehicle_LicensePlateExists() {
        when(vehicleRepository.existsByLicensePlate(anyString())).thenReturn(true);
        
        assertThrows(BadRequestException.class, () -> vehicleService.createVehicle(vehicleRequest));
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }
    
    @Test
    void testGetVehicleById_Success() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(testVehicle));
        
        VehicleDTO result = vehicleService.getVehicleById(1L);
        
        assertNotNull(result);
        assertEquals(testVehicle.getId(), result.getId());
        assertEquals(testVehicle.getLicensePlate(), result.getLicensePlate());
    }
    
    @Test
    void testGetVehicleById_NotFound() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> vehicleService.getVehicleById(1L));
    }
}

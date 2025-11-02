package com.mobilecarwash.service.impl;

import com.mobilecarwash.dto.BookingDTO;
import com.mobilecarwash.dto.BookingRequest;
import com.mobilecarwash.entity.Booking;
import com.mobilecarwash.entity.Service;
import com.mobilecarwash.entity.User;
import com.mobilecarwash.entity.Vehicle;
import com.mobilecarwash.exception.BadRequestException;
import com.mobilecarwash.exception.ResourceNotFoundException;
import com.mobilecarwash.repository.BookingRepository;
import com.mobilecarwash.repository.ServiceRepository;
import com.mobilecarwash.repository.UserRepository;
import com.mobilecarwash.repository.VehicleRepository;
import com.mobilecarwash.service.BookingService;
import com.mobilecarwash.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@Transactional
public class BookingServiceImpl implements BookingService {
    
    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private ServiceRepository serviceRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    @Override
    public BookingDTO createBooking(BookingRequest request) {
        logger.info("Creating new booking for service ID: {}", request.getServiceId());
        
        User currentUser = userService.getCurrentUser();
        
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + request.getVehicleId()));
        
        Service service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with ID: " + request.getServiceId()));
        
        if (!vehicle.getUser().getId().equals(currentUser.getId())) {
            throw new BadRequestException("You can only book services for your own vehicles");
        }
        
        Booking booking = new Booking();
        booking.setCustomer(currentUser);
        booking.setVehicle(vehicle);
        booking.setService(service);
        booking.setScheduledDateTime(request.getScheduledDateTime());
        booking.setAddress(request.getAddress());
        booking.setCity(request.getCity());
        booking.setPostalCode(request.getPostalCode());
        booking.setLatitude(request.getLatitude());
        booking.setLongitude(request.getLongitude());
        booking.setSpecialInstructions(request.getSpecialInstructions());
        booking.setTotalPrice(service.getBasePrice());
        booking.setStatus(Booking.BookingStatus.PENDING);
        
        Booking savedBooking = bookingRepository.save(booking);
        logger.info("Booking created successfully with ID: {}", savedBooking.getId());
        
        return convertToDTO(savedBooking);
    }
    
    @Override
    public BookingDTO getBookingById(Long id) {
        logger.info("Fetching booking with ID: {}", id);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));
        return convertToDTO(booking);
    }
    
    @Override
    public List<BookingDTO> getAllBookings() {
        logger.info("Fetching all bookings");
        return bookingRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<BookingDTO> getBookingsByCustomerId(Long customerId) {
        logger.info("Fetching bookings for customer ID: {}", customerId);
        return bookingRepository.findByCustomerId(customerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<BookingDTO> getBookingsByServiceProviderId(Long serviceProviderId) {
        logger.info("Fetching bookings for service provider ID: {}", serviceProviderId);
        return bookingRepository.findByServiceProviderId(serviceProviderId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<BookingDTO> getBookingsByStatus(Booking.BookingStatus status) {
        logger.info("Fetching bookings with status: {}", status);
        return bookingRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public BookingDTO updateBookingStatus(Long id, String status) {
        logger.info("Updating booking status for ID: {}", id);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));
        
        Booking.BookingStatus newStatus = Booking.BookingStatus.valueOf(status.toUpperCase());
        booking.setStatus(newStatus);
        
        if (newStatus == Booking.BookingStatus.COMPLETED) {
            booking.setCompletedAt(LocalDateTime.now());
        }
        
        Booking updatedBooking = bookingRepository.save(booking);
        logger.info("Booking status updated successfully for ID: {}", updatedBooking.getId());
        
        return convertToDTO(updatedBooking);
    }
    
    @Override
    public BookingDTO assignServiceProvider(Long bookingId, Long serviceProviderId) {
        logger.info("Assigning service provider {} to booking {}", serviceProviderId, bookingId);
        
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));
        
        User serviceProvider = userRepository.findById(serviceProviderId)
                .orElseThrow(() -> new ResourceNotFoundException("Service provider not found with ID: " + serviceProviderId));
        
        if (serviceProvider.getRole() != User.UserRole.SERVICE_PROVIDER) {
            throw new BadRequestException("User is not a service provider");
        }
        
        booking.setServiceProvider(serviceProvider);
        booking.setStatus(Booking.BookingStatus.ASSIGNED);
        
        Booking updatedBooking = bookingRepository.save(booking);
        logger.info("Service provider assigned successfully to booking ID: {}", updatedBooking.getId());
        
        return convertToDTO(updatedBooking);
    }
    
    @Override
    public void deleteBooking(Long id) {
        logger.info("Deleting booking with ID: {}", id);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));
        
        if (booking.getStatus() != Booking.BookingStatus.PENDING) {
            throw new BadRequestException("Cannot delete booking with status: " + booking.getStatus());
        }
        
        bookingRepository.delete(booking);
        logger.info("Booking deleted successfully with ID: {}", id);
    }
    
    private BookingDTO convertToDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setCustomerId(booking.getCustomer().getId());
        dto.setCustomerName(booking.getCustomer().getFirstName() + " " + booking.getCustomer().getLastName());
        
        if (booking.getServiceProvider() != null) {
            dto.setServiceProviderId(booking.getServiceProvider().getId());
            dto.setServiceProviderName(booking.getServiceProvider().getFirstName() + " " + booking.getServiceProvider().getLastName());
        }
        
        dto.setVehicleId(booking.getVehicle().getId());
        dto.setVehicleInfo(booking.getVehicle().getMake() + " " + booking.getVehicle().getModel() + " (" + booking.getVehicle().getLicensePlate() + ")");
        dto.setServiceId(booking.getService().getId());
        dto.setServiceName(booking.getService().getName());
        dto.setScheduledDateTime(booking.getScheduledDateTime());
        dto.setAddress(booking.getAddress());
        dto.setCity(booking.getCity());
        dto.setPostalCode(booking.getPostalCode());
        dto.setLatitude(booking.getLatitude());
        dto.setLongitude(booking.getLongitude());
        dto.setStatus(booking.getStatus().name());
        dto.setTotalPrice(booking.getTotalPrice());
        dto.setSpecialInstructions(booking.getSpecialInstructions());
        dto.setCompletedAt(booking.getCompletedAt());
        dto.setCreatedAt(booking.getCreatedAt());
        dto.setUpdatedAt(booking.getUpdatedAt());
        return dto;
    }
}

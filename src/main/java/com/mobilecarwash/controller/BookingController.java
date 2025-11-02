package com.mobilecarwash.controller;

import com.mobilecarwash.dto.BookingDTO;
import com.mobilecarwash.dto.BookingRequest;
import com.mobilecarwash.entity.Booking;
import com.mobilecarwash.service.BookingService;
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
import java.util.Map;

@RestController
@RequestMapping("/bookings")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Bookings", description = "Booking management endpoints")
public class BookingController {
    
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);
    
    @Autowired
    private BookingService bookingService;
    
    @PostMapping
    @Operation(summary = "Create a new booking")
    public ResponseEntity<BookingDTO> createBooking(@Valid @RequestBody BookingRequest request) {
        logger.info("Creating new booking");
        BookingDTO booking = bookingService.createBooking(request);
        return new ResponseEntity<>(booking, HttpStatus.CREATED);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all bookings (Admin only)")
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        logger.info("Fetching all bookings");
        List<BookingDTO> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get booking by ID")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long id) {
        logger.info("Fetching booking with ID: {}", id);
        BookingDTO booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(booking);
    }
    
    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get bookings by customer ID")
    public ResponseEntity<List<BookingDTO>> getBookingsByCustomerId(@PathVariable Long customerId) {
        logger.info("Fetching bookings for customer ID: {}", customerId);
        List<BookingDTO> bookings = bookingService.getBookingsByCustomerId(customerId);
        return ResponseEntity.ok(bookings);
    }
    
    @GetMapping("/provider/{serviceProviderId}")
    @Operation(summary = "Get bookings by service provider ID")
    public ResponseEntity<List<BookingDTO>> getBookingsByServiceProviderId(@PathVariable Long serviceProviderId) {
        logger.info("Fetching bookings for service provider ID: {}", serviceProviderId);
        List<BookingDTO> bookings = bookingService.getBookingsByServiceProviderId(serviceProviderId);
        return ResponseEntity.ok(bookings);
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Get bookings by status")
    public ResponseEntity<List<BookingDTO>> getBookingsByStatus(@PathVariable String status) {
        logger.info("Fetching bookings with status: {}", status);
        List<BookingDTO> bookings = bookingService.getBookingsByStatus(Booking.BookingStatus.valueOf(status.toUpperCase()));
        return ResponseEntity.ok(bookings);
    }
    
    @PutMapping("/{id}/status")
    @Operation(summary = "Update booking status")
    public ResponseEntity<BookingDTO> updateBookingStatus(@PathVariable Long id, @RequestBody Map<String, String> statusUpdate) {
        logger.info("Updating booking status for ID: {}", id);
        String status = statusUpdate.get("status");
        BookingDTO booking = bookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok(booking);
    }
    
    @PutMapping("/{bookingId}/assign/{serviceProviderId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SERVICE_PROVIDER')")
    @Operation(summary = "Assign service provider to booking")
    public ResponseEntity<BookingDTO> assignServiceProvider(@PathVariable Long bookingId, @PathVariable Long serviceProviderId) {
        logger.info("Assigning service provider to booking");
        BookingDTO booking = bookingService.assignServiceProvider(bookingId, serviceProviderId);
        return ResponseEntity.ok(booking);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete booking")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        logger.info("Deleting booking with ID: {}", id);
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}

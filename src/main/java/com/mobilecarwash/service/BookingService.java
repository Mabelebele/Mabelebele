package com.mobilecarwash.service;

import com.mobilecarwash.dto.BookingDTO;
import com.mobilecarwash.dto.BookingRequest;
import com.mobilecarwash.entity.Booking;

import java.util.List;

public interface BookingService {
    BookingDTO createBooking(BookingRequest request);
    
    BookingDTO getBookingById(Long id);
    
    List<BookingDTO> getAllBookings();
    
    List<BookingDTO> getBookingsByCustomerId(Long customerId);
    
    List<BookingDTO> getBookingsByServiceProviderId(Long serviceProviderId);
    
    List<BookingDTO> getBookingsByStatus(Booking.BookingStatus status);
    
    BookingDTO updateBookingStatus(Long id, String status);
    
    BookingDTO assignServiceProvider(Long bookingId, Long serviceProviderId);
    
    void deleteBooking(Long id);
}

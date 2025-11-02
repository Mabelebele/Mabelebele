package com.mobilecarwash.repository;

import com.mobilecarwash.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomerId(Long customerId);
    
    List<Booking> findByServiceProviderId(Long serviceProviderId);
    
    List<Booking> findByStatus(Booking.BookingStatus status);
    
    List<Booking> findByCustomerIdAndStatus(Long customerId, Booking.BookingStatus status);
    
    List<Booking> findByServiceProviderIdAndStatus(Long serviceProviderId, Booking.BookingStatus status);
    
    List<Booking> findByScheduledDateTimeBetween(LocalDateTime start, LocalDateTime end);
}

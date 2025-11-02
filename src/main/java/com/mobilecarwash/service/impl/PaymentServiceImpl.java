package com.mobilecarwash.service.impl;

import com.mobilecarwash.dto.PaymentDTO;
import com.mobilecarwash.dto.PaymentRequest;
import com.mobilecarwash.entity.Booking;
import com.mobilecarwash.entity.Payment;
import com.mobilecarwash.exception.BadRequestException;
import com.mobilecarwash.exception.ResourceNotFoundException;
import com.mobilecarwash.repository.BookingRepository;
import com.mobilecarwash.repository.PaymentRepository;
import com.mobilecarwash.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Override
    public PaymentDTO createPayment(PaymentRequest request) {
        logger.info("Creating new payment for booking ID: {}", request.getBookingId());
        
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + request.getBookingId()));
        
        if (paymentRepository.findByBookingId(request.getBookingId()).isPresent()) {
            throw new BadRequestException("Payment already exists for this booking");
        }
        
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(Payment.PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase()));
        payment.setStatus(Payment.PaymentStatus.PENDING);
        payment.setTransactionId(request.getTransactionId() != null ? request.getTransactionId() : UUID.randomUUID().toString());
        
        Payment savedPayment = paymentRepository.save(payment);
        logger.info("Payment created successfully with ID: {}", savedPayment.getId());
        
        return convertToDTO(savedPayment);
    }
    
    @Override
    public PaymentDTO getPaymentById(Long id) {
        logger.info("Fetching payment with ID: {}", id);
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + id));
        return convertToDTO(payment);
    }
    
    @Override
    public PaymentDTO getPaymentByBookingId(Long bookingId) {
        logger.info("Fetching payment for booking ID: {}", bookingId);
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for booking ID: " + bookingId));
        return convertToDTO(payment);
    }
    
    @Override
    public List<PaymentDTO> getAllPayments() {
        logger.info("Fetching all payments");
        return paymentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PaymentDTO> getPaymentsByStatus(Payment.PaymentStatus status) {
        logger.info("Fetching payments with status: {}", status);
        return paymentRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public PaymentDTO updatePaymentStatus(Long id, String status) {
        logger.info("Updating payment status for ID: {}", id);
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + id));
        
        Payment.PaymentStatus newStatus = Payment.PaymentStatus.valueOf(status.toUpperCase());
        payment.setStatus(newStatus);
        
        if (newStatus == Payment.PaymentStatus.COMPLETED) {
            payment.setPaidAt(LocalDateTime.now());
        }
        
        Payment updatedPayment = paymentRepository.save(payment);
        logger.info("Payment status updated successfully for ID: {}", updatedPayment.getId());
        
        return convertToDTO(updatedPayment);
    }
    
    @Override
    public void deletePayment(Long id) {
        logger.info("Deleting payment with ID: {}", id);
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + id));
        
        if (payment.getStatus() == Payment.PaymentStatus.COMPLETED) {
            throw new BadRequestException("Cannot delete completed payment");
        }
        
        paymentRepository.delete(payment);
        logger.info("Payment deleted successfully with ID: {}", id);
    }
    
    private PaymentDTO convertToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setBookingId(payment.getBooking().getId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod().name());
        dto.setStatus(payment.getStatus().name());
        dto.setTransactionId(payment.getTransactionId());
        dto.setPaidAt(payment.getPaidAt());
        dto.setCreatedAt(payment.getCreatedAt());
        return dto;
    }
}

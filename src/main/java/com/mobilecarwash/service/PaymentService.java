package com.mobilecarwash.service;

import com.mobilecarwash.dto.PaymentDTO;
import com.mobilecarwash.dto.PaymentRequest;
import com.mobilecarwash.entity.Payment;

import java.util.List;

public interface PaymentService {
    PaymentDTO createPayment(PaymentRequest request);
    
    PaymentDTO getPaymentById(Long id);
    
    PaymentDTO getPaymentByBookingId(Long bookingId);
    
    List<PaymentDTO> getAllPayments();
    
    List<PaymentDTO> getPaymentsByStatus(Payment.PaymentStatus status);
    
    PaymentDTO updatePaymentStatus(Long id, String status);
    
    void deletePayment(Long id);
}

package com.mobilecarwash.controller;

import com.mobilecarwash.dto.PaymentDTO;
import com.mobilecarwash.dto.PaymentRequest;
import com.mobilecarwash.entity.Payment;
import com.mobilecarwash.service.PaymentService;
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
@RequestMapping("/payments")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Payments", description = "Payment management endpoints")
public class PaymentController {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    
    @Autowired
    private PaymentService paymentService;
    
    @PostMapping
    @Operation(summary = "Create a new payment")
    public ResponseEntity<PaymentDTO> createPayment(@Valid @RequestBody PaymentRequest request) {
        logger.info("Creating new payment");
        PaymentDTO payment = paymentService.createPayment(request);
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all payments (Admin only)")
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        logger.info("Fetching all payments");
        List<PaymentDTO> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long id) {
        logger.info("Fetching payment with ID: {}", id);
        PaymentDTO payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }
    
    @GetMapping("/booking/{bookingId}")
    @Operation(summary = "Get payment by booking ID")
    public ResponseEntity<PaymentDTO> getPaymentByBookingId(@PathVariable Long bookingId) {
        logger.info("Fetching payment for booking ID: {}", bookingId);
        PaymentDTO payment = paymentService.getPaymentByBookingId(bookingId);
        return ResponseEntity.ok(payment);
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get payments by status (Admin only)")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByStatus(@PathVariable String status) {
        logger.info("Fetching payments with status: {}", status);
        List<PaymentDTO> payments = paymentService.getPaymentsByStatus(Payment.PaymentStatus.valueOf(status.toUpperCase()));
        return ResponseEntity.ok(payments);
    }
    
    @PutMapping("/{id}/status")
    @Operation(summary = "Update payment status")
    public ResponseEntity<PaymentDTO> updatePaymentStatus(@PathVariable Long id, @RequestBody Map<String, String> statusUpdate) {
        logger.info("Updating payment status for ID: {}", id);
        String status = statusUpdate.get("status");
        PaymentDTO payment = paymentService.updatePaymentStatus(id, status);
        return ResponseEntity.ok(payment);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete payment (Admin only)")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        logger.info("Deleting payment with ID: {}", id);
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}

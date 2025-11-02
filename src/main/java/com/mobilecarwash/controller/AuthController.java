package com.mobilecarwash.controller;

import com.mobilecarwash.dto.LoginRequest;
import com.mobilecarwash.dto.LoginResponse;
import com.mobilecarwash.dto.RegisterRequest;
import com.mobilecarwash.dto.UserDTO;
import com.mobilecarwash.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterRequest request) {
        logger.info("Registration request received for email: {}", request.getEmail());
        UserDTO user = userService.createUser(request);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    
    @PostMapping("/login")
    @Operation(summary = "Login user")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        logger.info("Login request received for email: {}", request.getEmail());
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }
}

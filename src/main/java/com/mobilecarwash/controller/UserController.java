package com.mobilecarwash.controller;

import com.mobilecarwash.dto.UserDTO;
import com.mobilecarwash.entity.User;
import com.mobilecarwash.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Users", description = "User management endpoints")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users (Admin only)")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        logger.info("Fetching all users");
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        logger.info("Fetching user with ID: {}", id);
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/email/{email}")
    @Operation(summary = "Get user by email")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        logger.info("Fetching user with email: {}", email);
        UserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get users by role (Admin only)")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable String role) {
        logger.info("Fetching users with role: {}", role);
        List<UserDTO> users = userService.getUsersByRole(User.UserRole.valueOf(role.toUpperCase()));
        return ResponseEntity.ok(users);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete user (Admin only)")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.info("Deleting user with ID: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

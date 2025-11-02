package com.mobilecarwash.service.impl;

import com.mobilecarwash.dto.*;
import com.mobilecarwash.entity.User;
import com.mobilecarwash.exception.BadRequestException;
import com.mobilecarwash.exception.ResourceNotFoundException;
import com.mobilecarwash.repository.UserRepository;
import com.mobilecarwash.security.JwtUtil;
import com.mobilecarwash.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public UserDTO createUser(RegisterRequest request) {
        logger.info("Creating new user with email: {}", request.getEmail());
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(User.UserRole.valueOf(request.getRole().toUpperCase()));
        user.setActive(true);
        
        User savedUser = userRepository.save(user);
        logger.info("User created successfully with ID: {}", savedUser.getId());
        
        return convertToDTO(savedUser);
    }
    
    @Override
    public UserDTO getUserById(Long id) {
        logger.info("Fetching user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return convertToDTO(user);
    }
    
    @Override
    public UserDTO getUserByEmail(String email) {
        logger.info("Fetching user with email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return convertToDTO(user);
    }
    
    @Override
    public List<UserDTO> getAllUsers() {
        logger.info("Fetching all users");
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<UserDTO> getUsersByRole(User.UserRole role) {
        logger.info("Fetching users with role: {}", role);
        return userRepository.findByRole(role).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public UserDTO updateUser(Long id, RegisterRequest request) {
        logger.info("Updating user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        
        user.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        
        User updatedUser = userRepository.save(user);
        logger.info("User updated successfully with ID: {}", updatedUser.getId());
        
        return convertToDTO(updatedUser);
    }
    
    @Override
    public void deleteUser(Long id) {
        logger.info("Deleting user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        user.setActive(false);
        userRepository.save(user);
        logger.info("User deactivated successfully with ID: {}", id);
    }
    
    @Override
    public LoginResponse login(LoginRequest request) {
        logger.info("Login attempt for user: {}", request.getEmail());
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String token = jwtUtil.generateToken(request.getEmail());
        
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        logger.info("Login successful for user: {}", request.getEmail());
        
        return new LoginResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().name()
        );
    }
    
    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadRequestException("No authenticated user found");
        }
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
    }
    
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole().name());
        dto.setActive(user.getActive());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}

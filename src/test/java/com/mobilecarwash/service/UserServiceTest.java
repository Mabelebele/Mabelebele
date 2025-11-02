package com.mobilecarwash.service;

import com.mobilecarwash.dto.LoginRequest;
import com.mobilecarwash.dto.LoginResponse;
import com.mobilecarwash.dto.RegisterRequest;
import com.mobilecarwash.dto.UserDTO;
import com.mobilecarwash.entity.User;
import com.mobilecarwash.exception.BadRequestException;
import com.mobilecarwash.exception.ResourceNotFoundException;
import com.mobilecarwash.repository.UserRepository;
import com.mobilecarwash.security.JwtUtil;
import com.mobilecarwash.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private AuthenticationManager authenticationManager;
    
    @Mock
    private JwtUtil jwtUtil;
    
    @InjectMocks
    private UserServiceImpl userService;
    
    private User testUser;
    private RegisterRequest registerRequest;
    
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setPhoneNumber("1234567890");
        testUser.setRole(User.UserRole.CUSTOMER);
        testUser.setActive(true);
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());
        
        registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        registerRequest.setPhoneNumber("1234567890");
        registerRequest.setRole("CUSTOMER");
    }
    
    @Test
    void testCreateUser_Success() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        UserDTO result = userService.createUser(registerRequest);
        
        assertNotNull(result);
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(testUser.getFirstName(), result.getFirstName());
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    void testCreateUser_EmailAlreadyExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        
        assertThrows(BadRequestException.class, () -> userService.createUser(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        
        UserDTO result = userService.getUserById(1L);
        
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getEmail(), result.getEmail());
    }
    
    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
    }
    
    @Test
    void testLogin_Success() {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password123");
        Authentication authentication = mock(Authentication.class);
        
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtil.generateToken(anyString())).thenReturn("jwt-token");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        
        LoginResponse result = userService.login(loginRequest);
        
        assertNotNull(result);
        assertEquals("jwt-token", result.getToken());
        assertEquals(testUser.getEmail(), result.getEmail());
    }
}

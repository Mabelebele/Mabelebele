package com.mobilecarwash.service;

import com.mobilecarwash.dto.*;
import com.mobilecarwash.entity.User;

import java.util.List;

public interface UserService {
    UserDTO createUser(RegisterRequest request);
    
    UserDTO getUserById(Long id);
    
    UserDTO getUserByEmail(String email);
    
    List<UserDTO> getAllUsers();
    
    List<UserDTO> getUsersByRole(User.UserRole role);
    
    UserDTO updateUser(Long id, RegisterRequest request);
    
    void deleteUser(Long id);
    
    LoginResponse login(LoginRequest request);
    
    User getCurrentUser();
}

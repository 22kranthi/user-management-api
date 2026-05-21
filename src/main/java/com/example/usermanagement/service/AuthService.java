package com.example.usermanagement.service;

import com.example.usermanagement.dto.AuthResponseDTO;
import com.example.usermanagement.dto.LoginDTO;
import com.example.usermanagement.dto.RegisterDTO;
import com.example.usermanagement.exception.InvalidCredentialsException;
import com.example.usermanagement.exception.UserAlreadyExistsException;
import com.example.usermanagement.exception.UserNotFoundException;


public interface AuthService {

    /**
     * Register new user
     * @param registerDTO contains email, password, confirmPassword
     * @return AuthResponseDTO with token
     * @throws UserAlreadyExistsException if email already exists
     */
    AuthResponseDTO register(RegisterDTO registerDTO);

    /**
     * Login user
     * @param loginDTO contains email, password
     * @return AuthResponseDTO with JWT token
     * @throws UserNotFoundException if user doesn't exist
     * @throws InvalidCredentialsException if password is wrong
     */
    AuthResponseDTO login(LoginDTO loginDTO);

    /**
     * Logout user (optional - for future use)
     * @param token JWT token to invalidate
     */
    void logout(String token);

    /**
     * Validate JWT token
     * @param token JWT token to validate
     * @return true if valid, false if invalid
     */
    boolean validateToken(String token);

    /**
     * Get user ID from token
     * @param token JWT token
     * @return user ID
     */
    Long getUserIdFromToken(String token);

    /**
     * Get email from token
     * @param token JWT token
     * @return user email
     */
    String getEmailFromToken(String token);

    /**
     * Get role from token
     * @param token JWT token
     * @return user role
     */
    String getRoleFromToken(String token);
}
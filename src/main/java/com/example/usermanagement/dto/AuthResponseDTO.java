package com.example.usermanagement.dto;

import java.time.LocalDateTime;

public class AuthResponseDTO {

    private String token;
    private String email;
    private String role;
    private Long userId;
    private String firstName;
    private String lastName;
    private boolean emailVerified;
    private LocalDateTime createdAt;

    // Constructors
    public AuthResponseDTO() {
    }

    public AuthResponseDTO(String token, String email, String role, Long userId) {
        this.token = token;
        this.email = email;
        this.role = role;
        this.userId = userId;
    }

    public AuthResponseDTO(String token, String email, String role, Long userId,
                           String firstName, String lastName, boolean emailVerified,
                           LocalDateTime createdAt) {
        this.token = token;
        this.email = email;
        this.role = role;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailVerified = emailVerified;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
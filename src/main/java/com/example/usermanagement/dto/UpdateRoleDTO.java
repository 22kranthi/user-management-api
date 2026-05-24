package com.example.usermanagement.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateRoleDTO {

    @NotBlank(message = "Role cannot be blank")
    private String role;  // "USER" or "ADMIN"

    // Constructors
    public UpdateRoleDTO() {
    }

    public UpdateRoleDTO(String role) {
        this.role = role;
    }

    // Getters and Setters
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
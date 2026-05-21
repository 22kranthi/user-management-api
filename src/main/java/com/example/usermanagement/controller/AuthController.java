package com.example.usermanagement.controller;

import com.example.usermanagement.dto.AuthResponseDTO;
import com.example.usermanagement.dto.LoginDTO;
import com.example.usermanagement.dto.MessageResponse;
import com.example.usermanagement.dto.RegisterDTO;
import com.example.usermanagement.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    // @Autowired
    private AuthService authService;

    // Constructor Injection
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Register new user
     * POST /api/v1/auth/register
     *
     * Request: RegisterDTO { email, password, confirmPassword }
     * Response: 201 Created with token
     * Error: 400 Bad Request (validation), 409 Conflict (user exists)
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO) {
        AuthResponseDTO response = authService.register(registerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Login user
     * POST /api/v1/auth/login
     *
     * Request: LoginDTO { email, password }
     * Response: 200 OK with token
     * Error: 400 Bad Request (validation), 401 Unauthorized (invalid creds), 404 Not Found (user)
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        AuthResponseDTO response = authService.login(loginDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Logout user
     * POST /api/v1/auth/logout
     *
     * Header: Authorization: Bearer <token>
     * Response: 200 OK
     * Error: 401 Unauthorized (invalid token)
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        // Remove "Bearer " prefix
        String cleanToken = token.replace("Bearer ", "");
        authService.logout(cleanToken);
        return ResponseEntity.ok(new MessageResponse("Logged out successfully"));
    }
}
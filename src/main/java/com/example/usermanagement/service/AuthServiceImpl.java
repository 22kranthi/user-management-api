package com.example.usermanagement.service;

import com.example.usermanagement.dto.AuthResponseDTO;
import com.example.usermanagement.dto.LoginDTO;
import com.example.usermanagement.dto.RegisterDTO;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.exception.InvalidCredentialsException;
import com.example.usermanagement.exception.UserAlreadyExistsException;
import com.example.usermanagement.exception.UserNotFoundException;
import com.example.usermanagement.repository.UserRepository;
import com.example.usermanagement.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    // @Autowired
    private UserRepository userRepository;

    // @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // Constructor injection
    public AuthServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public AuthResponseDTO register(RegisterDTO registerDTO) {
        // Step 1: Check if email already exists
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new UserAlreadyExistsException(
                    "User with email " + registerDTO.getEmail() + " already exists"
            );
        }

        // Step 2: Validate passwords match
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Step 3: Encrypt password
        String encryptedPassword = passwordEncoder.encode(registerDTO.getPassword());

        // Step 4: Create new user entity
        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setPassword(encryptedPassword);
        user.setRole("USER"); // Default role

        // Step 5: Save to database
        User savedUser = userRepository.save(user);

        // Step 6: Generate JWT token
        String token = jwtTokenProvider.generateToken(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole()
        );

        // Step 7: Return response with token
        return new AuthResponseDTO(
                token,
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getId()
        );
    }

    @Override
    public AuthResponseDTO login(LoginDTO loginDTO) {
        // Step 1: Find user by email
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException(
                        "User with email " + loginDTO.getEmail() + " not found"
                ));

        // Step 2: Verify password
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        // Step 3: Generate JWT token
        String token = jwtTokenProvider.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );

        // Step 4: Return response with token
        return new AuthResponseDTO(
                token,
                user.getEmail(),
                user.getRole(),
                user.getId()
        );
    }

    @Override
    public void logout(String token) {
        // Step 1: Validate token
        if (!validateToken(token)) {
            throw new InvalidCredentialsException("Invalid or expired token");
        }

        // Step 2: Add to blacklist (optional - implement later if needed)
        // For now, just validate the token

        // JWT tokens are stateless, so logout just means:
        // - Client deletes token from local storage
        // - Server validates on next request
        // - If token expired, user can't use it
    }

    @Override
    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    @Override
    public Long getUserIdFromToken(String token) {
        return jwtTokenProvider.getUserIdFromToken(token);
    }

    @Override
    public String getEmailFromToken(String token) {
        return jwtTokenProvider.getEmailFromToken(token);
    }

    @Override
    public String getRoleFromToken(String token) {
        return jwtTokenProvider.getRoleFromToken(token);
    }
}
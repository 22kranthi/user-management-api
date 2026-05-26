package com.example.usermanagement.service;

import com.example.usermanagement.dto.ChangePasswordDTO;
import com.example.usermanagement.dto.ForgotPasswordDTO;
import com.example.usermanagement.dto.ResetPasswordDTO;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.exception.InvalidTokenException;
import com.example.usermanagement.exception.PasswordMismatchException;
import com.example.usermanagement.exception.TokenExpiredException;
import com.example.usermanagement.exception.UserNotFoundException;
import com.example.usermanagement.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class PasswordResetServiceImpl implements PasswordResetService {

    // @Autowired
    private UserRepository userRepository;

    // @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public PasswordResetServiceImpl(UserRepository userRepository,
                                    BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void initiatePasswordReset(ForgotPasswordDTO forgotPasswordDTO) {
        // Step 1: Find user by email
        User user = userRepository.findByEmail(forgotPasswordDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException(
                        "User with email " + forgotPasswordDTO.getEmail() + " not found"
                ));

        // Step 2: Generate unique reset token
        String resetToken = UUID.randomUUID().toString();

        // Step 3: Set expiry (1 hour from now)
        LocalDateTime resetTokenExpiry = LocalDateTime.now().plusHours(1);

        // Step 4: Save to user
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(resetTokenExpiry);
        userRepository.save(user);

            // Step 5: Send email with reset link
            // TODO: Implement email service in Week 3+
            // String resetLink = "http://yourapp.com/reset-password?token=" + resetToken;
            // emailService.sendPasswordResetEmail(user.getEmail(), resetLink);

        System.out.println("Password reset token for " + user.getEmail() + ": " + resetToken);
    }

    @Override
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) {
        // Step 1: Find user by email
        User user = userRepository.findByEmail(resetPasswordDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException(
                        "User with email " + resetPasswordDTO.getEmail() + " not found"
                ));

        // Step 2: Validate passwords match
        if (!resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getConfirmPassword())) {
            throw new PasswordMismatchException("Passwords do not match");
        }

        // Step 3: Check if reset token exists
        if (user.getResetToken() == null || user.getResetToken().isEmpty()) {
            throw new InvalidTokenException("No reset token found. Request password reset first");
        }

        // Step 4: Verify reset token is correct
        if (!user.getResetToken().equals(resetPasswordDTO.getResetToken())) {
            throw new InvalidTokenException("Invalid reset token");
        }

        // Step 5: Check if token is expired
        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Reset token has expired. Request new reset link");
        }

        // Step 6: Encrypt new password
        String encryptedPassword = passwordEncoder.encode(resetPasswordDTO.getNewPassword());

        // Step 7: Update user password
        user.setPassword(encryptedPassword);

        // Step 8: Clear reset token
        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        // Step 9: Save to database
        userRepository.save(user);

        // Step 10: Send confirmation email
        // TODO: emailService.sendPasswordChangedEmail(user.getEmail());

        System.out.println("Password reset successful for " + user.getEmail());
    }

    @Override
    public void changePassword(String email, ChangePasswordDTO changePasswordDTO) {
        // Step 1: Find user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found"
                ));

        // Step 2: Verify old password is correct
        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new InvalidTokenException("Old password is incorrect");
        }

        // Step 3: Validate new passwords match
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            throw new PasswordMismatchException("Passwords do not match");
        }

        // Step 4: Encrypt new password
        String encryptedPassword = passwordEncoder.encode(changePasswordDTO.getNewPassword());

        // Step 5: Update password
        user.setPassword(encryptedPassword);

        // Step 6: Save to database
        userRepository.save(user);

        // Step 7: Send notification email
        // TODO: emailService.sendPasswordChangedEmail(user.getEmail());

        System.out.println("Password changed successfully for " + user.getEmail());
    }
}

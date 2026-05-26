package com.example.usermanagement.service;

import com.example.usermanagement.dto.ChangePasswordDTO;
import com.example.usermanagement.dto.ForgotPasswordDTO;
import com.example.usermanagement.dto.ResetPasswordDTO;

public interface PasswordResetService {

    /**
     * Initiate password reset
     * Generates reset token and sends email
     * @param forgotPasswordDTO contains email
     */
    void initiatePasswordReset(ForgotPasswordDTO forgotPasswordDTO);

    /**
     * Reset password with token
     * @param resetPasswordDTO contains email, token, newPassword
     */
    void resetPassword(ResetPasswordDTO resetPasswordDTO);

    /**
     * Change password for logged-in user
     * @param email user's email
     * @param changePasswordDTO contains oldPassword, newPassword
     */
    void changePassword(String email, ChangePasswordDTO changePasswordDTO);
}
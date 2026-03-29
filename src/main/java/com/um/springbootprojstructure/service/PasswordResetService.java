package com.um.springbootprojstructure.service;

public interface PasswordResetService {
    /**
     * Creates a reset token for a user and returns it.
     * In production this would be emailed; here we store it in-memory.
     */
    String createResetToken(Long userId);

    Long getUserIdByResetToken(String resetToken);

    void invalidateResetToken(String resetToken);
}

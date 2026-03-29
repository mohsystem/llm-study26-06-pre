package com.um.springbootprojstructure.service;

public interface MfaOtpService {
    String generateOtp(Long userId);
    boolean verifyOtp(Long userId, String otp);
    void invalidateOtp(Long userId);
}

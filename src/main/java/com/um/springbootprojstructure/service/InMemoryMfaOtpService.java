package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.config.MfaProperties;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryMfaOtpService implements MfaOtpService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private static class OtpRecord {
        final String otp;
        final Instant expiresAt;

        OtpRecord(String otp, Instant expiresAt) {
            this.otp = otp;
            this.expiresAt = expiresAt;
        }
    }

    private final Map<Long, OtpRecord> otps = new ConcurrentHashMap<>();
    private final MfaProperties props;

    public InMemoryMfaOtpService(MfaProperties props) {
        this.props = props;
    }

    @Override
    public String generateOtp(Long userId) {
        String otp = randomDigits(props.getOtp().getLength());
        Instant expiresAt = Instant.now().plusSeconds(props.getOtp().getTtlSeconds());
        otps.put(userId, new OtpRecord(otp, expiresAt));
        return otp;
    }

    @Override
    public boolean verifyOtp(Long userId, String otp) {
        if (userId == null || otp == null) return false;
        OtpRecord rec = otps.get(userId);
        if (rec == null) return false;
        if (Instant.now().isAfter(rec.expiresAt)) return false;
        return rec.otp.equals(otp);
    }

    @Override
    public void invalidateOtp(Long userId) {
        if (userId == null) return;
        otps.remove(userId);
    }

    private String randomDigits(int length) {
        int safeLen = Math.max(4, Math.min(length, 10));
        StringBuilder sb = new StringBuilder(safeLen);
        for (int i = 0; i < safeLen; i++) {
            sb.append(SECURE_RANDOM.nextInt(10));
        }
        return sb.toString();
    }
}

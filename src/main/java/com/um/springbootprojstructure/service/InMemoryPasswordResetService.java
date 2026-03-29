package com.um.springbootprojstructure.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryPasswordResetService implements PasswordResetService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private static class ResetRecord {
        final Long userId;
        final Instant createdAt;

        ResetRecord(Long userId, Instant createdAt) {
            this.userId = userId;
            this.createdAt = createdAt;
        }
    }

    private final Map<String, ResetRecord> tokens = new ConcurrentHashMap<>();

    @Override
    public String createResetToken(Long userId) {
        String token = generateToken();
        tokens.put(token, new ResetRecord(userId, Instant.now()));
        return token;
    }

    @Override
    public Long getUserIdByResetToken(String resetToken) {
        if (resetToken == null || resetToken.isBlank()) return null;
        ResetRecord rec = tokens.get(resetToken);
        return rec == null ? null : rec.userId;
    }

    @Override
    public void invalidateResetToken(String resetToken) {
        if (resetToken == null) return;
        tokens.remove(resetToken);
    }

    private String generateToken() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}

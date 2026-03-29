package com.um.springbootprojstructure.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryPreAuthService implements PreAuthService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private static class Record {
        final Long userId;
        final Instant createdAt;

        Record(Long userId, Instant createdAt) {
            this.userId = userId;
            this.createdAt = createdAt;
        }
    }

    private final Map<String, Record> tokens = new ConcurrentHashMap<>();

    @Override
    public String createPreAuthToken(Long userId) {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        tokens.put(token, new Record(userId, Instant.now()));
        return token;
    }

    @Override
    public Long getUserId(String preAuthToken) {
        if (preAuthToken == null || preAuthToken.isBlank()) return null;
        Record rec = tokens.get(preAuthToken);
        return rec == null ? null : rec.userId;
    }

    @Override
    public void invalidate(String preAuthToken) {
        if (preAuthToken == null) return;
        tokens.remove(preAuthToken);
    }
}

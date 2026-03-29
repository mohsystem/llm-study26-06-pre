package com.um.springbootprojstructure.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemorySessionService implements SessionService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private static class SessionRecord {
        final Long userId;
        final Instant createdAt;

        SessionRecord(Long userId, Instant createdAt) {
            this.userId = userId;
            this.createdAt = createdAt;
        }
    }

    private final Map<String, SessionRecord> sessions = new ConcurrentHashMap<>();

    @Override
    public String createSession(Long userId) {
        String token = generateToken();
        sessions.put(token, new SessionRecord(userId, Instant.now()));
        return token;
    }

    @Override
    public Long getUserIdByToken(String token) {
        if (token == null || token.isBlank()) return null;
        SessionRecord rec = sessions.get(token);
        return rec == null ? null : rec.userId;
    }

    @Override
    public void invalidate(String token) {
        if (token == null) return;
        sessions.remove(token);
    }

    private String generateToken() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}

package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.*;
import com.um.springbootprojstructure.entity.ApiKey;
import com.um.springbootprojstructure.entity.User;
import com.um.springbootprojstructure.repository.ApiKeyRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.List;

@Service
public class ApiKeyServiceImpl implements ApiKeyService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final CurrentUserService currentUserService;
    private final ApiKeyRepository apiKeyRepository;
    private final PasswordEncoder passwordEncoder; // reuse BCrypt to hash secrets

    public ApiKeyServiceImpl(CurrentUserService currentUserService,
                             ApiKeyRepository apiKeyRepository,
                             PasswordEncoder passwordEncoder) {
        this.currentUserService = currentUserService;
        this.apiKeyRepository = apiKeyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public ApiKeyCreateResponse issue(String authorizationHeader, ApiKeyCreateRequest request) {
        User user = currentUserService.requireUserByBearerToken(authorizationHeader);

        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("name is required");
        }

        String rawSecret = generateSecret();
        String prefix = rawSecret.substring(0, Math.min(8, rawSecret.length()));

        ApiKey apiKey = new ApiKey();
        apiKey.setUser(user);
        apiKey.setName(request.getName());
        apiKey.setKeyPrefix(prefix);
        apiKey.setSecretHash(passwordEncoder.encode(rawSecret));
        apiKey.setActive(true);

        ApiKey saved = apiKeyRepository.save(apiKey);

        // Return raw secret only once
        return new ApiKeyCreateResponse(saved.getId(), "API_KEY_ISSUED", rawSecret);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApiKeyItemResponse> list(String authorizationHeader) {
        User user = currentUserService.requireUserByBearerToken(authorizationHeader);

        return apiKeyRepository.findAllByUser_IdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(k -> new ApiKeyItemResponse(
                        k.getId(),
                        k.getName(),
                        k.getKeyPrefix(),
                        k.isActive(),
                        k.getCreatedAt(),
                        k.getRevokedAt()
                ))
                .toList();
    }

    @Override
    @Transactional
    public ApiKeyRevokeResponse revoke(String authorizationHeader, Long keyId) {
        User user = currentUserService.requireUserByBearerToken(authorizationHeader);

        ApiKey key = apiKeyRepository.findByIdAndUser_Id(keyId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("api key not found: " + keyId));

        key.setActive(false);
        key.setRevokedAt(Instant.now());
        apiKeyRepository.save(key);

        return new ApiKeyRevokeResponse(key.getId(), "API_KEY_REVOKED");
    }

    private String generateSecret() {
        byte[] bytes = new byte[36];
        SECURE_RANDOM.nextBytes(bytes);
        return "ak_" + Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}

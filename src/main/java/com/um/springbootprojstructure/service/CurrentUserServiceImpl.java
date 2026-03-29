package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.entity.User;
import com.um.springbootprojstructure.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {

    private final SessionService sessionService;
    private final UserRepository userRepository;

    public CurrentUserServiceImpl(SessionService sessionService, UserRepository userRepository) {
        this.sessionService = sessionService;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public User requireUserByBearerToken(String authorizationHeader) {
        String token = extractBearerToken(authorizationHeader);
        if (token == null) {
            throw new IllegalArgumentException("missing or invalid Authorization header");
        }

        Long userId = sessionService.getUserIdByToken(token);
        if (userId == null) {
            throw new IllegalArgumentException("invalid or expired session token");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));

        if (!user.isActive()) {
            throw new IllegalArgumentException("user is inactive");
        }

        return user;
    }

    private String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader == null) return null;
        String prefix = "Bearer ";
        if (!authorizationHeader.startsWith(prefix)) return null;
        String token = authorizationHeader.substring(prefix.length()).trim();
        return token.isBlank() ? null : token;
    }
}

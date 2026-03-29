package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.*;
import com.um.springbootprojstructure.entity.User;
import com.um.springbootprojstructure.entity.Role;
import com.um.springbootprojstructure.repository.UserRepository;
import com.um.springbootprojstructure.service.exception.DuplicateAccountException;
import com.um.springbootprojstructure.service.exception.NotFoundException;
import com.um.springbootprojstructure.service.exception.RejectedOperationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;
    private final CurrentUserService currentUserService;
    private final PasswordResetService passwordResetService;
    private final PreAuthService preAuthService;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           SessionService sessionService,
                           CurrentUserService currentUserService,
                           PasswordResetService passwordResetService,
                           PreAuthService preAuthService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.sessionService = sessionService;
        this.currentUserService = currentUserService;
        this.passwordResetService = passwordResetService;
        this.preAuthService = preAuthService;
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new IllegalArgumentException("username is required");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("email is required");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("password is required");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateAccountException("DUPLICATE_USERNAME", "username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateAccountException("DUPLICATE_EMAIL", "email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFullName(request.getUsername()); // satisfy non-null
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setActive(true);

        User saved = userRepository.save(user);
        return new RegisterResponse(saved.getId(), "REGISTERED");
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        if (request.getIdentifier() == null || request.getIdentifier().isBlank()) {
            throw new IllegalArgumentException("identifier (username/email) is required");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("password is required");
        }

        User user = userRepository.findByUsernameIgnoreCase(request.getIdentifier())
                .or(() -> userRepository.findByEmailIgnoreCase(request.getIdentifier()))
                .orElseThrow(() -> new RejectedOperationException("AUTH_INVALID_CREDENTIALS", "invalid credentials"));

        if (!user.isActive()) {
            throw new RejectedOperationException("AUTH_USER_INACTIVE", "user is inactive");
        }

        boolean ok = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());
        if (!ok) {
            throw new RejectedOperationException("AUTH_INVALID_CREDENTIALS", "invalid credentials");
        }

        String preAuthToken = preAuthService.createPreAuthToken(user.getId());
        return new LoginResponse("MFA_REQUIRED", null, preAuthToken);
    }

    @Override
    public StatusResponse changePassword(String authorizationHeader, ChangePasswordRequest request) {
        if (request.getCurrentPassword() == null || request.getCurrentPassword().isBlank()) {
            throw new IllegalArgumentException("currentPassword is required");
        }
        if (request.getNewPassword() == null || request.getNewPassword().isBlank()) {
            throw new IllegalArgumentException("newPassword is required");
        }
        if (request.getNewPassword().length() < 8) {
            throw new IllegalArgumentException("newPassword must be at least 8 characters");
        }

        User user = currentUserService.requireUserByBearerToken(authorizationHeader);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new RejectedOperationException("AUTH_CURRENT_PASSWORD_INCORRECT", "currentPassword is incorrect");
        }

        // Optionally prevent reusing same password
        if (passwordEncoder.matches(request.getNewPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("newPassword must be different from current password");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return new StatusResponse("PASSWORD_CHANGED");
    }

    @Override
    public StatusResponse requestPasswordReset(ResetRequest request) {
        if (request.getIdentifier() == null || request.getIdentifier().isBlank()) {
            throw new IllegalArgumentException("identifier (username/email) is required");
        }

        // Don't reveal whether the user exists (always return same status).
        userRepository.findByUsernameIgnoreCase(request.getIdentifier())
                .or(() -> userRepository.findByEmailIgnoreCase(request.getIdentifier()))
                .ifPresent(user -> {
                    if (user.isActive()) {
                        passwordResetService.createResetToken(user.getId());
                        // In a real system, email the token/link to the user here.
                    }
                });

        return new StatusResponse("RESET_REQUESTED");
    }

    @Override
    public StatusResponse confirmPasswordReset(ResetConfirmRequest request) {
        if (request.getResetToken() == null || request.getResetToken().isBlank()) {
            throw new IllegalArgumentException("resetToken is required");
        }
        if (request.getNewPassword() == null || request.getNewPassword().isBlank()) {
            throw new IllegalArgumentException("newPassword is required");
        }
        if (request.getNewPassword().length() < 8) {
            throw new IllegalArgumentException("newPassword must be at least 8 characters");
        }

        Long userId = passwordResetService.getUserIdByResetToken(request.getResetToken());
        if (userId == null) {
            throw new RejectedOperationException("AUTH_RESET_TOKEN_INVALID", "invalid or expired resetToken");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND", "user not found"));

        if (!user.isActive()) {
            throw new RejectedOperationException("AUTH_USER_INACTIVE", "user is inactive");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        passwordResetService.invalidateResetToken(request.getResetToken());

        return new StatusResponse("PASSWORD_RESET");
    }
}

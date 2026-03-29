package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.MfaChallengeRequest;
import com.um.springbootprojstructure.dto.MfaChallengeResponse;
import com.um.springbootprojstructure.dto.MfaVerifyRequest;
import com.um.springbootprojstructure.dto.MfaVerifyResponse;
import com.um.springbootprojstructure.entity.User;
import com.um.springbootprojstructure.repository.UserRepository;
import com.um.springbootprojstructure.service.exception.RejectedOperationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MfaServiceImpl implements MfaService {

    private final PreAuthService preAuthService;
    private final MfaOtpService mfaOtpService;
    private final NotificationGatewayClient notificationGatewayClient;
    private final UserRepository userRepository;
    private final SessionService sessionService;

    public MfaServiceImpl(PreAuthService preAuthService,
                          MfaOtpService mfaOtpService,
                          NotificationGatewayClient notificationGatewayClient,
                          UserRepository userRepository,
                          SessionService sessionService) {
        this.preAuthService = preAuthService;
        this.mfaOtpService = mfaOtpService;
        this.notificationGatewayClient = notificationGatewayClient;
        this.userRepository = userRepository;
        this.sessionService = sessionService;
    }

    @Override
    @Transactional
    public MfaChallengeResponse challenge(MfaChallengeRequest request) {
        if (request.getPreAuthToken() == null || request.getPreAuthToken().isBlank()) {
            throw new IllegalArgumentException("preAuthToken is required");
        }

        Long userId = preAuthService.getUserId(request.getPreAuthToken());
        if (userId == null) {
            throw new RejectedOperationException("AUTH_PREAUTH_TOKEN_INVALID", "invalid or expired preAuthToken");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));

        if (!user.isActive()) {
            throw new RejectedOperationException("AUTH_USER_INACTIVE", "user is inactive");
        }

        String otp = mfaOtpService.generateOtp(user.getId());
        notificationGatewayClient.sendSms(
                user.getPhoneNumber(),
                "Your verification code is: " + otp
        );

        return new MfaChallengeResponse("CHALLENGE_SENT");
    }

    @Override
    @Transactional
    public MfaVerifyResponse verify(MfaVerifyRequest request) {
        if (request.getPreAuthToken() == null || request.getPreAuthToken().isBlank()) {
            throw new IllegalArgumentException("preAuthToken is required");
        }
        if (request.getPasscode() == null || request.getPasscode().isBlank()) {
            throw new IllegalArgumentException("passcode is required");
        }

        Long userId = preAuthService.getUserId(request.getPreAuthToken());
        if (userId == null) {
            throw new RejectedOperationException("AUTH_PREAUTH_TOKEN_INVALID", "invalid or expired preAuthToken");
        }

        boolean ok = mfaOtpService.verifyOtp(userId, request.getPasscode());
        if (!ok) {
            throw new RejectedOperationException("AUTH_OTP_INVALID", "invalid or expired passcode");
        }

        // One-time usage cleanup
        mfaOtpService.invalidateOtp(userId);
        preAuthService.invalidate(request.getPreAuthToken());

        String sessionToken = sessionService.createSession(userId);
        return new MfaVerifyResponse("AUTHENTICATED", sessionToken);
    }
}

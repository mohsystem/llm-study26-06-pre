package com.um.springbootprojstructure.controller;

import com.um.springbootprojstructure.dto.*;
import com.um.springbootprojstructure.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/change-password")
    public ResponseEntity<StatusResponse> changePassword(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody ChangePasswordRequest request
    ) {
        return ResponseEntity.ok(authService.changePassword(authorization, request));
    }

    @PostMapping("/reset-request")
    public ResponseEntity<StatusResponse> resetRequest(@RequestBody ResetRequest request) {
        return ResponseEntity.ok(authService.requestPasswordReset(request));
    }

    @PostMapping("/reset-confirm")
    public ResponseEntity<StatusResponse> resetConfirm(@RequestBody ResetConfirmRequest request) {
        return ResponseEntity.ok(authService.confirmPasswordReset(request));
    }
}

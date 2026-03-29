package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.*;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    StatusResponse changePassword(String authorizationHeader, ChangePasswordRequest request);

    StatusResponse requestPasswordReset(ResetRequest request);
    StatusResponse confirmPasswordReset(ResetConfirmRequest request);
}

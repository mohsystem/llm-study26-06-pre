package com.um.springbootprojstructure.service;

public interface PreAuthService {
    String createPreAuthToken(Long userId);
    Long getUserId(String preAuthToken);
    void invalidate(String preAuthToken);
}

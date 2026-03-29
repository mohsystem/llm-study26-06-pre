package com.um.springbootprojstructure.service;

public interface SessionService {
    String createSession(Long userId);
    Long getUserIdByToken(String token);
    void invalidate(String token);
}

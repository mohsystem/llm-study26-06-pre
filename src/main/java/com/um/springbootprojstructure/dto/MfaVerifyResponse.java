package com.um.springbootprojstructure.dto;

public class MfaVerifyResponse {
    private String status;
    private String token; // final session token

    public MfaVerifyResponse() {}

    public MfaVerifyResponse(String status, String token) {
        this.status = status;
        this.token = token;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}

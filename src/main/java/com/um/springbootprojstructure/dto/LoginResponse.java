package com.um.springbootprojstructure.dto;

public class LoginResponse {
    private String status;

    // for non-MFA or legacy; not used in MFA-required flow
    private String token;

    // returned when MFA is required
    private String preAuthToken;

    public LoginResponse() {}

    public LoginResponse(String status, String token, String preAuthToken) {
        this.status = status;
        this.token = token;
        this.preAuthToken = preAuthToken;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getPreAuthToken() { return preAuthToken; }
    public void setPreAuthToken(String preAuthToken) { this.preAuthToken = preAuthToken; }
}

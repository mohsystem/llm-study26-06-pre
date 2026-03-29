package com.um.springbootprojstructure.dto;

public class MfaChallengeRequest {
    private String preAuthToken;

    public MfaChallengeRequest() {}

    public String getPreAuthToken() { return preAuthToken; }
    public void setPreAuthToken(String preAuthToken) { this.preAuthToken = preAuthToken; }
}

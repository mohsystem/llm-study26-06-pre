package com.um.springbootprojstructure.dto;

public class MfaVerifyRequest {
    private String preAuthToken;
    private String passcode;

    public MfaVerifyRequest() {}

    public String getPreAuthToken() { return preAuthToken; }
    public void setPreAuthToken(String preAuthToken) { this.preAuthToken = preAuthToken; }

    public String getPasscode() { return passcode; }
    public void setPasscode(String passcode) { this.passcode = passcode; }
}

package com.um.springbootprojstructure.dto;

public class ResetRequest {
    private String identifier; // username or email

    public ResetRequest() {}

    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }
}

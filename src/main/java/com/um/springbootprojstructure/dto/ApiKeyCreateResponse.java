package com.um.springbootprojstructure.dto;

public class ApiKeyCreateResponse {
    private Long keyId;
    private String status;

    // returned only once
    private String apiKey;

    public ApiKeyCreateResponse() {}

    public ApiKeyCreateResponse(Long keyId, String status, String apiKey) {
        this.keyId = keyId;
        this.status = status;
        this.apiKey = apiKey;
    }

    public Long getKeyId() { return keyId; }
    public void setKeyId(Long keyId) { this.keyId = keyId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
}

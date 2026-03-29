package com.um.springbootprojstructure.dto;

public class ApiKeyRevokeResponse {
    private Long keyId;
    private String status;

    public ApiKeyRevokeResponse() {}

    public ApiKeyRevokeResponse(Long keyId, String status) {
        this.keyId = keyId;
        this.status = status;
    }

    public Long getKeyId() { return keyId; }
    public void setKeyId(Long keyId) { this.keyId = keyId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

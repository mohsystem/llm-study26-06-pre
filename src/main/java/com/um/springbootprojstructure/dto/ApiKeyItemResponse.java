package com.um.springbootprojstructure.dto;

import java.time.Instant;

public class ApiKeyItemResponse {
    private Long keyId;
    private String name;
    private String keyPrefix;
    private boolean active;
    private Instant createdAt;
    private Instant revokedAt;

    public ApiKeyItemResponse() {}

    public ApiKeyItemResponse(Long keyId, String name, String keyPrefix, boolean active, Instant createdAt, Instant revokedAt) {
        this.keyId = keyId;
        this.name = name;
        this.keyPrefix = keyPrefix;
        this.active = active;
        this.createdAt = createdAt;
        this.revokedAt = revokedAt;
    }

    public Long getKeyId() { return keyId; }
    public void setKeyId(Long keyId) { this.keyId = keyId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getKeyPrefix() { return keyPrefix; }
    public void setKeyPrefix(String keyPrefix) { this.keyPrefix = keyPrefix; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getRevokedAt() { return revokedAt; }
    public void setRevokedAt(Instant revokedAt) { this.revokedAt = revokedAt; }
}

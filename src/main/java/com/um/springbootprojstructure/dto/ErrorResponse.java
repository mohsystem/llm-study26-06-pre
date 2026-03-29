package com.um.springbootprojstructure.dto;

import java.time.Instant;

public class ErrorResponse {
    private String status;     // e.g. "REJECTED"
    private String errorCode;  // e.g. "DUPLICATE_USERNAME"
    private String message;    // human-readable
    private String path;
    private Instant timestamp;

    public ErrorResponse() {}

    public ErrorResponse(String status, String errorCode, String message, String path, Instant timestamp) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
        this.path = path;
        this.timestamp = timestamp;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}

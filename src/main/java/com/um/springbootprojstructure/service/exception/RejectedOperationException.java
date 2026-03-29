package com.um.springbootprojstructure.service.exception;

public class RejectedOperationException extends RuntimeException {
    private final String errorCode;

    public RejectedOperationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}

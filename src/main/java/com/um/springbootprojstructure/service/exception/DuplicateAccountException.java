package com.um.springbootprojstructure.service.exception;

public class DuplicateAccountException extends RuntimeException {
    private final String errorCode;

    public DuplicateAccountException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}

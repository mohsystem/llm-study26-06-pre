package com.um.springbootprojstructure.config;

import com.um.springbootprojstructure.dto.ErrorResponse;
import com.um.springbootprojstructure.service.exception.DuplicateAccountException;
import com.um.springbootprojstructure.service.exception.NotFoundException;
import com.um.springbootprojstructure.service.exception.RejectedOperationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateAccountException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateAccountException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "REJECTED", ex.getErrorCode(), ex.getMessage(), req);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, "REJECTED", ex.getErrorCode(), ex.getMessage(), req);
    }

    @ExceptionHandler(RejectedOperationException.class)
    public ResponseEntity<ErrorResponse> handleRejected(RejectedOperationException ex, HttpServletRequest req) {
        // Use 401 for auth-related codes, otherwise 400.
        HttpStatus status = ex.getErrorCode() != null && ex.getErrorCode().startsWith("AUTH_")
                ? HttpStatus.UNAUTHORIZED
                : HttpStatus.BAD_REQUEST;

        return build(status, "REJECTED", ex.getErrorCode(), ex.getMessage(), req);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "REJECTED", "VALIDATION_ERROR", ex.getMessage(), req);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAny(Exception ex, HttpServletRequest req) {
        // avoid leaking internal details; keep deterministic output
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "REJECTED", "INTERNAL_ERROR",
                "An unexpected error occurred", req);
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus httpStatus,
                                               String status,
                                               String errorCode,
                                               String message,
                                               HttpServletRequest req) {
        ErrorResponse body = new ErrorResponse(
                status,
                errorCode,
                message,
                req.getRequestURI(),
                Instant.now()
        );
        return ResponseEntity.status(httpStatus).body(body);
    }
}

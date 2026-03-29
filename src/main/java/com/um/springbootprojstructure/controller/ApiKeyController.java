package com.um.springbootprojstructure.controller;

import com.um.springbootprojstructure.dto.*;
import com.um.springbootprojstructure.service.ApiKeyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/api-keys")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    public ApiKeyController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @PostMapping
    public ResponseEntity<ApiKeyCreateResponse> issue(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody ApiKeyCreateRequest request
    ) {
        return ResponseEntity.ok(apiKeyService.issue(authorization, request));
    }

    @GetMapping
    public ResponseEntity<List<ApiKeyItemResponse>> list(
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        return ResponseEntity.ok(apiKeyService.list(authorization));
    }

    @DeleteMapping("/{keyId}")
    public ResponseEntity<ApiKeyRevokeResponse> revoke(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long keyId
    ) {
        return ResponseEntity.ok(apiKeyService.revoke(authorization, keyId));
    }
}

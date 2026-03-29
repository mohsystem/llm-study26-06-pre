package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.*;

import java.util.List;

public interface ApiKeyService {
    ApiKeyCreateResponse issue(String authorizationHeader, ApiKeyCreateRequest request);
    List<ApiKeyItemResponse> list(String authorizationHeader);
    ApiKeyRevokeResponse revoke(String authorizationHeader, Long keyId);
}

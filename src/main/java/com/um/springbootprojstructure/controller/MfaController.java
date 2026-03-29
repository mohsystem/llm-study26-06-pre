package com.um.springbootprojstructure.controller;

import com.um.springbootprojstructure.dto.MfaChallengeRequest;
import com.um.springbootprojstructure.dto.MfaChallengeResponse;
import com.um.springbootprojstructure.dto.MfaVerifyRequest;
import com.um.springbootprojstructure.dto.MfaVerifyResponse;
import com.um.springbootprojstructure.service.MfaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/mfa")
public class MfaController {

    private final MfaService mfaService;

    public MfaController(MfaService mfaService) {
        this.mfaService = mfaService;
    }

    @PostMapping("/challenge")
    public ResponseEntity<MfaChallengeResponse> challenge(@RequestBody MfaChallengeRequest request) {
        return ResponseEntity.ok(mfaService.challenge(request));
    }

    @PostMapping("/verify")
    public ResponseEntity<MfaVerifyResponse> verify(@RequestBody MfaVerifyRequest request) {
        return ResponseEntity.ok(mfaService.verify(request));
    }
}

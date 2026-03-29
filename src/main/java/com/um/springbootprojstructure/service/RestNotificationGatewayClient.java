package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.config.MfaProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class RestNotificationGatewayClient implements NotificationGatewayClient {

    private final RestClient restClient;
    private final MfaProperties props;

    public RestNotificationGatewayClient(RestClient restClient, MfaProperties props) {
        this.restClient = restClient;
        this.props = props;
    }

    @Override
    public void sendSms(String phoneNumber, String message) {
        if (props.getGateway().getUrl() == null || props.getGateway().getUrl().isBlank()) {
            throw new IllegalArgumentException("mfa.gateway.url is not configured");
        }
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("user phoneNumber is missing");
        }

        // Generic payload; adjust to match your external gateway contract
        Map<String, Object> payload = Map.of(
                "to", phoneNumber,
                "message", message
        );

        restClient.post()
                .uri(props.getGateway().getUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .toBodilessEntity();
    }
}

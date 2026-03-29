package com.um.springbootprojstructure.service;

public interface NotificationGatewayClient {
    void sendSms(String phoneNumber, String message);
}

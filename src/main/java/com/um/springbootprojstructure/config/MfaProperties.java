package com.um.springbootprojstructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mfa")
public class MfaProperties {
    private Otp otp = new Otp();
    private Gateway gateway = new Gateway();

    public Otp getOtp() { return otp; }
    public void setOtp(Otp otp) { this.otp = otp; }

    public Gateway getGateway() { return gateway; }
    public void setGateway(Gateway gateway) { this.gateway = gateway; }

    public static class Otp {
        private int length = 6;
        private long ttlSeconds = 300;

        public int getLength() { return length; }
        public void setLength(int length) { this.length = length; }

        public long getTtlSeconds() { return ttlSeconds; }
        public void setTtlSeconds(long ttlSeconds) { this.ttlSeconds = ttlSeconds; }
    }

    public static class Gateway {
        private String url;

        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
    }
}

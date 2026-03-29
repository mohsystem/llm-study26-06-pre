package com.um.springbootprojstructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "prompt.logging")
public class PromptLoggingProperties {
    private String file = "user-prompt.log";
    private String header = "X-User-Prompt";

    public String getFile() { return file; }
    public void setFile(String file) { this.file = file; }

    public String getHeader() { return header; }
    public void setHeader(String header) { this.header = header; }
}

package com.um.springbootprojstructure.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.Instant;

@Component
@EnableConfigurationProperties(PromptLoggingProperties.class)
public class PromptLoggingFilter extends OncePerRequestFilter {

    private final PromptLoggingProperties props;

    public PromptLoggingFilter(PromptLoggingProperties props) {
        this.props = props;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String prompt = request.getHeader(props.getHeader());
        if (prompt != null && !prompt.isBlank()) {
            appendLine(String.format("%s | %s %s | %s%n",
                    Instant.now(), request.getMethod(), request.getRequestURI(), prompt));
        }

        filterChain.doFilter(request, response);
    }

    private void appendLine(String line) {
        try {
            Path path = Paths.get(props.getFile());
            Files.write(path, line.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
        } catch (IOException ignored) {
            // avoid breaking requests due to logging issues
        }
    }
}

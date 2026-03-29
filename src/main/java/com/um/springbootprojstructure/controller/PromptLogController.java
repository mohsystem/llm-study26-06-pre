package com.um.springbootprojstructure.controller;

import com.um.springbootprojstructure.config.PromptLoggingProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/prompt-log")
public class PromptLogController {

    private final PromptLoggingProperties props;

    public PromptLogController(PromptLoggingProperties props) {
        this.props = props;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> append(@RequestBody String promptText) throws Exception {
        if (promptText == null) promptText = "";
        String line = String.format("%s | PROMPT | %s%n", Instant.now(), promptText.replaceAll("\\R", " "));
        Path path = Paths.get(props.getFile());

        Files.write(path, line.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);

        return ResponseEntity.ok(Map.of("logged", true, "file", path.toAbsolutePath().toString()));
    }
}

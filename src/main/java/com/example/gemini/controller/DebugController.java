package com.example.gemini.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @GetMapping("/config")
    public Map<String, Object> getConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("apiUrl", apiUrl);

        if (apiKey != null && !apiKey.isEmpty()) {
            config.put("apiKeyConfigured", true);
            config.put("apiKeyLength", apiKey.length());
            config.put("apiKeyPrefix", apiKey.length() > 4 ? apiKey.substring(0, 4) : "****");
            config.put("apiKeySuffix", apiKey.length() > 4 ? apiKey.substring(apiKey.length() - 4) : "****");
            config.put("startsWithAIza", apiKey.startsWith("AIza"));
        } else {
            config.put("apiKeyConfigured", false);
            config.put("apiKeyLength", 0);
        }

        return config;
    }
}


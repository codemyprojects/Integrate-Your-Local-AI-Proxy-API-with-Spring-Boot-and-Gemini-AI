package com.example.gemini.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @GetMapping("/config")
    public Map<String, Object> getConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("apiUrl", apiUrl);
        config.put("apiKeyConfigured", apiKey != null && !apiKey.isEmpty() && !apiKey.equals("your-api-key-here"));
        config.put("apiKeyLength", apiKey != null ? apiKey.length() : 0);
        return config;
    }
}

package com.example.gemini.service;

import com.example.gemini.dto.GeminiApiRequest;
import com.example.gemini.dto.GeminiApiResponse;
import com.example.gemini.dto.GeminiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class GeminiService {

    private static final Logger logger = LoggerFactory.getLogger(GeminiService.class);

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent}")
    private String apiUrl;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public GeminiService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public GeminiResponse generateContent(String text) throws Exception {
        logger.info("Generating content for text: {}", text);
        logger.info("Using API URL: {}", apiUrl);

        // Debug API key (show first/last few characters only for security)
        if (apiKey != null && apiKey.length() > 10) {
            logger.info("API Key format: {}...{} (length: {})",
                    apiKey.substring(0, 4),
                    apiKey.substring(apiKey.length() - 4),
                    apiKey.length());
        } else {
            logger.error("API Key is too short or null: {}", apiKey);
        }

        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your-api-key-here")) {
            throw new RuntimeException("Gemini API key is not configured. Please set GEMINI_API_KEY environment variable.");
        }

        // Validate API key format
        if (!apiKey.startsWith("AIza")) {
            throw new RuntimeException("Invalid API key format. Gemini API keys should start with 'AIza'");
        }

        // Create the request payload
        GeminiApiRequest.Part part = new GeminiApiRequest.Part(text);
        GeminiApiRequest.Content content = new GeminiApiRequest.Content(List.of(part));
        GeminiApiRequest requestPayload = new GeminiApiRequest(List.of(content));

        // Convert to JSON
        String jsonPayload = objectMapper.writeValueAsString(requestPayload);
        logger.info("Request payload: {}", jsonPayload);

        // Build the HTTP request
        String fullUrl = apiUrl + "?key=" + apiKey;
        logger.info("Making request to: {}", apiUrl + "?key=" + apiKey.substring(0, 4) + "...");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .timeout(Duration.ofSeconds(60))
                .build();

        // Send the request
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        logger.info("Response status: {}", response.statusCode());
        logger.info("Response body: {}", response.body());

        if (response.statusCode() == 200) {
            // Parse the response
            GeminiApiResponse apiResponse = objectMapper.readValue(response.body(), GeminiApiResponse.class);

            if (apiResponse.getCandidates() != null && !apiResponse.getCandidates().isEmpty()) {
                String generatedText = apiResponse.getCandidates().get(0).getContent().getParts().get(0).getText();
                logger.info("Successfully generated content: {}", generatedText);
                return new GeminiResponse(generatedText, true);
            } else {
                logger.warn("No candidates in response");
                return new GeminiResponse("No content generated", false);
            }
        } else {
            String errorMsg = "API call failed with status: " + response.statusCode() + ", body: " + response.body();
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
    }
}
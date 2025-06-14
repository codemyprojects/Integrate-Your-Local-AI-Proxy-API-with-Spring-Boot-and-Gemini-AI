package com.example.gemini.controller;

import com.example.gemini.dto.GeminiRequest;
import com.example.gemini.dto.GeminiResponse;
import com.example.gemini.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/gemini")
@CrossOrigin(origins = "*")
public class GeminiController {

    private static final Logger logger = LoggerFactory.getLogger(GeminiController.class);

    @Autowired
    private GeminiService geminiService;

    @Value("${gemini.api.key}")
    private String apiKey;

    @PostMapping("/generate")
    public ResponseEntity<GeminiResponse> generateContent(@RequestBody GeminiRequest request) {
        logger.info("POST request received with text: {}", request.getText());

        if (!isApiKeyConfigured()) {
            return ResponseEntity.badRequest()
                    .body(new GeminiResponse(getApiKeyErrorMessage(), false));
        }

        try {
            GeminiResponse response = geminiService.generateContent(request.getText());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error processing POST request: ", e);
            return ResponseEntity.internalServerError()
                    .body(new GeminiResponse("Error: " + e.getMessage(), false));
        }
    }

    @GetMapping("/generate")
    public ResponseEntity<GeminiResponse> generateContentGet(@RequestParam String text) {
        logger.info("GET request received with text: {}", text);

        if (!isApiKeyConfigured()) {
            return ResponseEntity.badRequest()
                    .body(new GeminiResponse(getApiKeyErrorMessage(), false));
        }

        try {
            if (text == null || text.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new GeminiResponse("Text parameter is required", false));
            }

            GeminiResponse response = geminiService.generateContent(text);
            logger.info("Successfully processed GET request");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error processing GET request: ", e);
            return ResponseEntity.internalServerError()
                    .body(new GeminiResponse("Error: " + e.getMessage(), false));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        logger.info("Health check requested");
        String status = isApiKeyConfigured() ? "✅ Ready" : "❌ API Key Not Configured";
        return ResponseEntity.ok("Gemini API Service is running! Status: " + status);
    }

    @GetMapping("/setup")
    public ResponseEntity<String> setup() {
        String currentStatus = isApiKeyConfigured() ? "✅ API Key Configured" : "❌ API Key Not Set";

        String htmlResponse = "<html>" +
                "<head><title>Gemini API Setup</title></head>" +
                "<body>" +
                "<h1>🔧 Gemini API Setup Guide</h1>" +

                "<h2>Step 1: Get Your API Key</h2>" +
                "<ol>" +
                "<li>Go to <a href=\"https://makersuite.google.com/app/apikey\" target=\"_blank\">Google AI Studio</a></li>" +
                "<li>Sign in with your Google account</li>" +
                "<li>Click \"Create API Key\"</li>" +
                "<li>Copy your API key (starts with \"AIza...\")</li>" +
                "</ol>" +

                "<h2>Step 2: Set Environment Variable</h2>" +

                "<h3>Windows (Command Prompt):</h3>" +
                "<pre>set GEMINI_API_KEY=your-actual-api-key-here</pre>" +

                "<h3>Windows (PowerShell):</h3>" +
                "<pre>$env:GEMINI_API_KEY=\"your-actual-api-key-here\"</pre>" +

                "<h3>Mac/Linux:</h3>" +
                "<pre>export GEMINI_API_KEY=your-actual-api-key-here</pre>" +

                "<h2>Step 3: Restart Your Application</h2>" +
                "<p>After setting the environment variable, restart your Spring Boot application.</p>" +

                "<h2>Step 4: Test</h2>" +
                "<p><a href=\"/api/gemini/health\">Check Health Status</a></p>" +

                "<h2>Alternative: Set in application.properties</h2>" +
                "<p>You can also add this line to your application.properties file:</p>" +
                "<pre>gemini.api.key=your-actual-api-key-here</pre>" +

                "<p><strong>Current Status:</strong> " + currentStatus + "</p>" +
                "</body>" +
                "</html>";

        return ResponseEntity.ok(htmlResponse);
    }

    private boolean isApiKeyConfigured() {
        return apiKey != null && !apiKey.isEmpty() && !apiKey.equals("your-api-key-here") && !apiKey.equals("${GEMINI_API_KEY:your-api-key-here}");
    }

    private String getApiKeyErrorMessage() {
        return "Gemini API key is not configured. Please:\n" +
                "1. Get your API key from https://makersuite.google.com/app/apikey\n" +
                "2. Set environment variable: GEMINI_API_KEY=your-actual-key\n" +
                "3. Restart the application\n" +
                "4. Or visit /api/gemini/setup for detailed instructions";
    }
}




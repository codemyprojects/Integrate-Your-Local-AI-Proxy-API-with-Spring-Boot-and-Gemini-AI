package com.example.gemini.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        Map<String, Object> errorResponse = new HashMap<>();

        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        String errorMessage = (String) request.getAttribute("jakarta.servlet.error.message");
        String requestUri = (String) request.getAttribute("jakarta.servlet.error.request_uri");

        if (statusCode == null) {
            statusCode = 500;
        }

        errorResponse.put("status", statusCode);
        errorResponse.put("error", HttpStatus.valueOf(statusCode).getReasonPhrase());
        errorResponse.put("message", errorMessage != null ? errorMessage : "An unexpected error occurred");
        errorResponse.put("path", requestUri);
        errorResponse.put("availableEndpoints", new String[]{
                "GET /api/gemini/health - Health check",
                "GET /api/gemini/generate?text=your-prompt - Generate content via GET",
                "POST /api/gemini/generate - Generate content via POST with JSON body"
        });

        return ResponseEntity.status(statusCode).body(errorResponse);
    }
}

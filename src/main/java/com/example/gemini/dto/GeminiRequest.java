package com.example.gemini.dto;


public class GeminiRequest {
    private String text;

    public GeminiRequest() {}

    public GeminiRequest(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

package com.example.gemini.dto;

public class GeminiResponse {
    private String content;
    private boolean success;

    public GeminiResponse() {}

    public GeminiResponse(String content, boolean success) {
        this.content = content;
        this.success = success;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

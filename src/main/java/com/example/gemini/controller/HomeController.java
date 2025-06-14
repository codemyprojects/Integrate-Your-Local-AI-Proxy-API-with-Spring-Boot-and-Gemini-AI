package com.example.gemini.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    @ResponseBody
    public String home() {
        return """
            <html>
            <head><title>Gemini API Service</title></head>
            <body>
                <h1>🤖 Gemini API Service</h1>
                <p>Service is running successfully!</p>
                
                <h2>Available Endpoints:</h2>
                <ul>
                    <li><strong>GET /api/gemini/health</strong> - Health check</li>
                    <li><strong>GET /api/gemini/generate?text=your-prompt</strong> - Generate content via GET</li>
                    <li><strong>POST /api/gemini/generate</strong> - Generate content via POST</li>
                </ul>
                
                <h2>Quick Test:</h2>
                <p><a href="/api/gemini/health">Test Health Endpoint</a></p>
                <p><a href="/api/gemini/generate?text=Hello, how are you?">Test Generate Endpoint</a></p>
                
                <h2>Example POST Request:</h2>
                <pre>
curl -X POST http://localhost:8080/api/gemini/generate \\
  -H "Content-Type: application/json" \\
  -d '{"text": "Explain how AI works in a few words"}'
                </pre>
            </body>
            </html>
            """;
    }
}

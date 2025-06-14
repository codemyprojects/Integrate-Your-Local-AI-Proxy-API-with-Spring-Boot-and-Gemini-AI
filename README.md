# Integrate-Your-Local-AI-Proxy-API-with-Spring-Boot-and-Gemini-AI

This project is a Spring Boot-based web API designed to act as a proxy or integration layer between your local application and Google's Gemini AI API. It enables developers to interact with Gemini's text generation capabilities using a clean, local REST interface, abstracting away direct calls to the external Gemini endpoint.

✅ Key Features

    Spring Boot application to handle local API requests.

    Uses a controller-service-DTO structure for clean architecture.

    Accepts user prompts via REST API and forwards them to the Gemini API.

    Handles responses and errors cleanly and returns results to the user.

    Includes debugging and test controllers for local development.

🏗️ Components

    GeminiApiApplication.java: Main entry point.

    GeminiController.java: REST controller that handles incoming prompt requests.

    GeminiService.java: Encapsulates the logic to communicate with the Gemini API.

    DTOs (GeminiRequest, GeminiResponse, etc.): Represent request/response structures.

    WebConfig.java: Configuration for CORS and web-related setup.

    Additional controllers (HomeController, TestController, DebugController): Support testing and debugging during local development.

🚀 How It Works

    Send a prompt via HTTP POST to a local endpoint (e.g., /api/gemini).

    The controller validates and processes the input.

    The request is forwarded to Gemini API using the provided API key.

    Gemini’s response is transformed and returned back to the client.

🧰 Use Cases

    Prototyping AI-based features (e.g., chatbots, content generation)

    Teaching and demoing LLM integration with enterprise Java stacks

    Running local LLM clients without exposing your API key in the frontend

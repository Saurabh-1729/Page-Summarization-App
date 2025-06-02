package com.pageSummary.Backend.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.util.Map;

@Service
public class GeminiService {

    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public GeminiService(WebClient.Builder WebClientBuilder) {
        this.webClient = WebClientBuilder.build();
    }

    public String generateAIResponse(String prompt) {
        try {
            Map<String, Object> requestBody = Map.of(
                    "contents", new Object[] {
                            Map.of("parts", new Object[]{
                                    Map.of("text", prompt)
                            })
                    }
            );

            String url = geminiApiUrl + geminiApiKey;

            String response = webClient.post()
                    .uri(url)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Log the raw response for debugging
//            System.out.println("Raw Gemini API response: " + response);
            return response;

        } catch (WebClientException e) {
//            System.err.println("Gemini API request failed: " + e.getMessage());
            // Return a JSON structure that matches what SummaryService expects
            return createErrorJsonResponse("Service temporarily unavailable. Please try again later.");

        } catch (Exception e) {
//            System.err.println("Unexpected error in Gemini service: " + e.getMessage());
            e.printStackTrace();
            // Return a JSON structure that matches what SummaryService expects
            return createErrorJsonResponse("Service temporarily unavailable. Please try again later.");
        }
    }

    private String createErrorJsonResponse(String errorMessage) {
        // Create a JSON response that matches Gemini's expected structure
        return String.format("""
            {
                "candidates": [
                    {
                        "content": {
                            "parts": [
                                {
                                    "text": "%s"
                                }
                            ]
                        }
                    }
                ]
            }
            """, errorMessage);
    }
}

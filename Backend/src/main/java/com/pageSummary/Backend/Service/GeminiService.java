//package com.pageSummary.Backend.Service;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.util.Map;
//
//@Service
//public class GeminiService {
//
//    private final WebClient webClient;
//
//    @Value("${gemini.api.url}")
//    private String geminiApiUrl;
//
//    @Value("${gemini.api.key}")
//    private String geminiApiKey;
//
//
//    public GeminiService(WebClient.Builder WebClientBuilder) {
//        this.webClient = WebClientBuilder.build();
//    }
//
//    public String generateAIResponse(String prompt) {
//        Map<String, Object> requestBody = Map.of(
//                "contents", new Object[] {
//                        Map.of("parts", new Object[]{
//                                Map.of("text", prompt)
//                        })
//                }
//        );
//
//        String url = geminiApiUrl + geminiApiKey;
//
//        String response = webClient.post()
//                .uri(url)
//                .header("Content-Type", "application/json")
//                .bodyValue(requestBody)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//
//        return response;
//    }
//}


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

            return response;

        } catch (WebClientException e) {
            // Log the error for debugging
            System.err.println("Gemini API request failed: " + e.getMessage());
            return "Service temporarily unavailable. Please try again later.";

        } catch (Exception e) {
            // Catch any other unexpected errors
            System.err.println("Unexpected error in Gemini service: " + e.getMessage());
            return "Service temporarily unavailable. Please try again later.";
        }
    }
}

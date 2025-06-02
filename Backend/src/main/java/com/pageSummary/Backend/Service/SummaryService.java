package com.pageSummary.Backend.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.pageSummary.Backend.DTO.RequestDTO;
import com.pageSummary.Backend.Model.SummaryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SummaryService {


    private final GeminiService geminiService;

    public SummaryService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    public SummaryResponse generateSummary(RequestDTO request) {
          String prompt = createPrompt(request);
          String AIResponse = geminiService.generateAIResponse(prompt);
          return processAIResponse(AIResponse);
    }

    private SummaryResponse processAIResponse(String AIResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(AIResponse);

            // Navigate through the JSON structure
            String extractedText = rootNode
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

            SummaryResponse response = new SummaryResponse();
            response.setSummary(extractedText);
            response.setStatus("success");
            return response;

        } catch (Exception e) {
            SummaryResponse errorResponse = new SummaryResponse();
            errorResponse.setSummary("Error parsing AI response");
            errorResponse.setStatus("error");
            return errorResponse;
        }
    }


    private String createPrompt(RequestDTO request) {
        return "Summarize the following texts highlighting the important parts of the text:  " + request.getText();
    }
}

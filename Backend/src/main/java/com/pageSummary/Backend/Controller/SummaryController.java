package com.pageSummary.Backend.Controller;

import com.pageSummary.Backend.DTO.RequestDTO;
import com.pageSummary.Backend.Model.SummaryResponse;
import com.pageSummary.Backend.Service.SummaryService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {

    @Autowired
    private SummaryService SummaryService;

    @PostMapping("/getSummary")
    @RateLimiter(name = "summaryService", fallbackMethod = "fallbackSummary")
    public ResponseEntity<SummaryResponse> getSummary(@RequestBody RequestDTO request) {
         SummaryResponse summaryResponse = SummaryService.generateSummary(request);
         return ResponseEntity.ok(summaryResponse);
    }

    public ResponseEntity<SummaryResponse> fallbackSummary(RequestDTO request, Exception ex) {
        SummaryResponse response = new SummaryResponse();
        response.setSummary("Rate limit exceeded. Please try again in a minute.");
        response.setStatus("rate_limited");
        return ResponseEntity.ok(response);
    }

}

package com.example.loanmanagement.controller;

import com.example.loanmanagement.dtos.LoanStatsResponse;
import com.example.loanmanagement.services.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService){
        this.statsService=statsService;
    }
    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    public ResponseEntity<LoanStatsResponse> getLoanStats() {
        LoanStatsResponse stats = statsService.getLoanStats();
        return ResponseEntity.ok(stats);
    }
}

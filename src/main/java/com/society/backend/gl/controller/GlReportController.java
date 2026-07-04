package com.society.backend.gl.controller;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.society.backend.gl.dto.TrialBalanceDTO;
import com.society.backend.gl.service.TrialBalanceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/gl/reports")
@RequiredArgsConstructor
@CrossOrigin("*")
public class GlReportController {

    private final TrialBalanceService trialBalanceService;

    // =========================
    // TRIAL BALANCE API
    // =========================

@GetMapping("/trial-balance/{societyId}")
public ResponseEntity<List<TrialBalanceDTO>> getTrialBalance(
        @PathVariable Long societyId,
        @RequestParam Long financialYearId) {

    return ResponseEntity.ok(
            trialBalanceService.getTrialBalance(societyId, financialYearId)
    );
}



}

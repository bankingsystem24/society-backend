package com.society.backend.gl.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.society.backend.gl.dto.TrialBalanceDTO;
import com.society.backend.gl.service.TrialBalanceService;

@RestController
@RequestMapping("/api/gl/reports")
@CrossOrigin("*")
public class GlReportController {

    @Autowired
    private TrialBalanceService trialBalanceService;

    // =========================
    // TRIAL BALANCE API
    // =========================

    @GetMapping("/trial-balance/{societyId}")
    public ResponseEntity<List<TrialBalanceDTO>> getTrialBalance(
            @PathVariable Long societyId) {

        return ResponseEntity.ok(
                trialBalanceService.getTrialBalance(societyId)
        );
    }
}

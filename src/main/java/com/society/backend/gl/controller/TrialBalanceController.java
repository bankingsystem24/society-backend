package com.society.backend.gl.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.society.backend.gl.dto.TrialBalanceDTO;
import com.society.backend.gl.service.TrialBalanceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/gl/reports")
@RequiredArgsConstructor
@CrossOrigin
public class TrialBalanceController {

    private final TrialBalanceService trialBalanceService;

    @GetMapping("/trial-balance")
    public List<TrialBalanceDTO> getTrialBalance(
            @RequestParam Long societyId) {
               
        return trialBalanceService.getTrialBalance(societyId);
    }
}
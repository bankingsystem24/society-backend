package com.society.backend.gl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.society.backend.gl.dto.TrialBalanceDTO;
import com.society.backend.gl.service.TrialBalanceService;

@RestController
@RequestMapping("/api/gl/reports")
@CrossOrigin
public class TrialBalanceController {

    @Autowired
    private TrialBalanceService trialBalanceService;

    @GetMapping("/trial-balance")
    public List<TrialBalanceDTO> getTrialBalance(
            @RequestParam Long societyId) {
                System.out.println("Received request for Trial Balance with Society ID: " + societyId);
                

        return trialBalanceService.getTrialBalance(societyId);
    }
}
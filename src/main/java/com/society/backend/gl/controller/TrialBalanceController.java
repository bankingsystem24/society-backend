package com.society.backend.gl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.society.backend.gl.dto.TrialBalanceDTO;
import com.society.backend.gl.repository.LedgerBalanceRepository;

@RestController
@RequestMapping("/api/gl/reports")
@CrossOrigin
public class TrialBalanceController {

    @Autowired
    private LedgerBalanceRepository ledgerBalanceRepository;

    @GetMapping("/trial-balance")
    public List<TrialBalanceDTO> getTrialBalance(@RequestParam Long societyId) {
        return ledgerBalanceRepository.getTrialBalance(societyId);
    }
}
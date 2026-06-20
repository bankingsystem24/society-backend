package com.society.backend.gl.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.society.backend.gl.service.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/profit-loss")
    public ResponseEntity<?> getProfitAndLoss(
            @RequestParam Long societyId,Long financialYearId) {

        return ResponseEntity.ok(
                reportService.getProfitAndLoss(
                        societyId,financialYearId
                )
        );
    }
    
}

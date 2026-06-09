package com.society.backend.gl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.society.backend.gl.service.ReportService;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin("*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/profit-loss")
    public ResponseEntity<?> getProfitAndLoss(
            @RequestParam Long societyId) {

        return ResponseEntity.ok(
                reportService.getProfitAndLoss(
                        societyId
                )
        );
    }
    
}

package com.society.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.society.backend.dto.BillGenerateRequest;
import com.society.backend.dto.BillingFilterRequest;
import com.society.backend.entity.Billing;
import com.society.backend.service.BillingService;

@RestController
@RequestMapping("/api/billing")
@CrossOrigin("*")
public class BillingController {

    @Autowired
    private BillingService billingService;

    // =========================
    // GENERATE MONTHLY BILLS
    // =========================

    @PostMapping("/generate")
    public String generateBills(
            @RequestBody BillGenerateRequest request) {

        return billingService.generateMonthlyBills(
                request.getSocietyId(),
                request.getMonth(),
                request.getYear()
        );
    }

    // =========================
    // GET SOCIETY BILLS
    // =========================

    @GetMapping("/society/{societyId}")
    public ResponseEntity<List<Billing>> getBySociety(
            @PathVariable Long societyId) {

        return ResponseEntity.ok(
                billingService.getBySociety(societyId));
    }

    // =========================
    // GET PENDING BILLS
    // =========================

    @GetMapping("/pending/{societyId}")
    public ResponseEntity<List<Billing>> getPending(
            @PathVariable Long societyId) {

        return ResponseEntity.ok(
                billingService.getPending(societyId));
    }

    // =========================
    // VIEW ALL BILLS WITH FILTER
    // =========================

    @PostMapping("/viewAllBills")
    public ResponseEntity<List<Billing>> viewAllBills(
            @RequestBody BillingFilterRequest request) {

        return ResponseEntity.ok(
                billingService.viewAllBills(
                        request.getSocietyId(),
                        request.getFlatId(),
                        request.getFromYear(),
                        request.getMonth(),
                        request.getStatus()
                )
        );
    }
}
package com.society.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.society.backend.entity.Billing;
import com.society.backend.service.BillingService;

@RestController
@RequestMapping("/api/billing")
@CrossOrigin("*")
public class BillingController {

    @Autowired
    private BillingService billingService;

    // 🔥 AUTO GENERATE MONTHLY BILLS
    @PostMapping("/generate")
    public ResponseEntity<String> generateBills(
            @RequestParam Long societyId,
            @RequestParam String month,
            @RequestParam int year) {

        String response = billingService.generateMonthlyBills(societyId, month, year);
        return ResponseEntity.ok(response);
    }

    // 📌 GET ALL BILLS OF SOCIETY
    @GetMapping("/society/{societyId}")
    public ResponseEntity<List<Billing>> getBySociety(@PathVariable Long societyId) {
        return ResponseEntity.ok(billingService.getBySociety(societyId));
    }

    // 📌 GET PENDING BILLS
    @GetMapping("/pending/{societyId}")
    public ResponseEntity<List<Billing>> getPending(@PathVariable Long societyId) {
        return ResponseEntity.ok(billingService.getPending(societyId));
    }
}
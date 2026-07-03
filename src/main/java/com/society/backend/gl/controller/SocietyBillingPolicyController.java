package com.society.backend.gl.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.society.backend.gl.dto.SocietyBillingPolicyDTO;
import com.society.backend.gl.entity.SocietyBillingPolicy;
import com.society.backend.gl.service.SocietyBillingPolicyService;

@RestController
@RequestMapping("/api/billing-policy")
@CrossOrigin(origins = "*")
public class SocietyBillingPolicyController {

    private final SocietyBillingPolicyService service;

    public SocietyBillingPolicyController(
            SocietyBillingPolicyService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SocietyBillingPolicy> save(
            @RequestBody SocietyBillingPolicyDTO dto) {

        return ResponseEntity.ok(service.save(dto));
    }

    @GetMapping
    public ResponseEntity<List<SocietyBillingPolicy>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SocietyBillingPolicy> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/society/{societyId}/financial-year/{financialYearId}")
    public ResponseEntity<SocietyBillingPolicy> getBySocietyAndFinancialYear(
            @PathVariable Long societyId,
            @PathVariable Long financialYearId) {

        return ResponseEntity.ok(service.getBySocietyAndFinancialYear(societyId, financialYearId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id) {

        service.delete(id);
        return ResponseEntity.ok("Policy deleted successfully");
    }
}
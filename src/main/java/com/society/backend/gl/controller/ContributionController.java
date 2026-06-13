package com.society.backend.gl.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.society.backend.dto.CompulsoryContributionRequest;
import com.society.backend.gl.service.ContributionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/contribution")
@RequiredArgsConstructor
public class ContributionController {

    private final ContributionService contributionService;

    @GetMapping("/compulsory/{societyId}/{financialYearId}")
    public ResponseEntity<?> getCompulsoryContributions(
            @PathVariable Long societyId,
            @PathVariable Long financialYearId
    ) {
        return ResponseEntity.ok(
                contributionService.getCompulsoryContributions(societyId,financialYearId)
        );
    }

    @PostMapping("/compulsory/{societyId}/{financialYearId}")
    public ResponseEntity<?> createCompulsoryContribution(
            @PathVariable Long societyId,
            @PathVariable Long financialYearId,
            @RequestBody CompulsoryContributionRequest request
    ) {
        contributionService.createCompulsoryContribution(societyId, financialYearId,request);
        return ResponseEntity.ok("Compulsory contribution created successfully");
    }
}

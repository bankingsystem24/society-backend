package com.society.backend.gl.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.society.backend.dto.CompulsoryContributionRequest;
import com.society.backend.dto.ContributionOrderRequest;
import com.society.backend.dto.VerifyContributionPaymentRequest;
import com.society.backend.gl.dto.ContributionPaymentRequest;
import com.society.backend.gl.service.ContributionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/contribution")
@RequiredArgsConstructor
public class ContributionController {

        private final ContributionService contributionService;

        @GetMapping("/{societyId}/{financialYearId}")
        public ResponseEntity<?> getContributions(
                        @PathVariable Long societyId,
                        @PathVariable Long financialYearId) {
                return ResponseEntity.ok(
                                contributionService.getContributions(societyId, financialYearId));
        }

        @PostMapping("/compulsory/{societyId}/{financialYearId}")
        public ResponseEntity<?> createCompulsoryContribution(
                        @PathVariable Long societyId,
                        @PathVariable Long financialYearId,
                        @RequestBody CompulsoryContributionRequest request) {
                contributionService.createCompulsoryContribution(societyId, financialYearId, request);
                return ResponseEntity.ok("Compulsory contribution created successfully");
        }

        @PostMapping("/voluntary/{societyId}/{financialYearId}")
        public ResponseEntity<?> createVoluntaryContribution(
                        @PathVariable Long societyId,
                        @PathVariable Long financialYearId,
                        @RequestBody CompulsoryContributionRequest request) {
                contributionService.createVoluntaryContribution(societyId, financialYearId, request);
                return ResponseEntity.ok("Voluntary contribution created successfully");
        }

        @PutMapping("/pay")
        public ResponseEntity<String> pay(
                        @RequestBody ContributionPaymentRequest request) {

                contributionService.pay(
                                request.getContributionIds(),
                                request.getPaymentMode(),
                                request.getFinancialYearId(),
                                request.getContributionAmount(),
                                request.getUserId());

                return ResponseEntity.ok("Payment successful");
        }

        @PostMapping("/create-order")
        public ResponseEntity<?> createOrder(
                        @RequestBody ContributionOrderRequest request) {

                return ResponseEntity.ok(
                                contributionService.createOrder(request));
        }

        @PostMapping("/verify-payment")
        public ResponseEntity<?> verifyPayment(
                        @RequestBody VerifyContributionPaymentRequest request) {

                contributionService.verifyPayment(request);

                return ResponseEntity.ok("Payment verified successfully");
        }
}

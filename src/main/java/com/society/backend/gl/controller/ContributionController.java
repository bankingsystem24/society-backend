package com.society.backend.gl.controller;

import java.time.LocalDate;
import java.util.List;

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
import com.society.backend.dto.ManualPaymentRequest;
import com.society.backend.dto.VerifyContributionPaymentRequest;
import com.society.backend.entity.Receipt;
import com.society.backend.enums.PaymentStatus;
import com.society.backend.gl.dto.ContributionPaymentRequest;
import com.society.backend.gl.entity.Contribution;
import com.society.backend.gl.repository.ContributionRepository;
import com.society.backend.gl.service.ContributionService;
import com.society.backend.repository.ReceiptRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/contribution")
@RequiredArgsConstructor
public class ContributionController {

        private final ContributionService contributionService;
        private final ContributionRepository contributionRepository;
        private final ReceiptRepository receiptRepository;

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
                                request.getUserId(),
                                request.getGlCreditAccount(),
                                request.getGlCreditAccount());

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

        @PostMapping("/manual-payment")
        public ResponseEntity<?> manualContributionPayment(
                        @RequestBody ManualPaymentRequest req) {

                try {

                        Long financialYearId = req.getFinancialYearId();

                        List<Contribution> contributions = contributionRepository.findByIdIn(req.getContributionIds());

                        if (contributions == null || contributions.isEmpty()) {
                                return ResponseEntity.badRequest()
                                                .body("No contribution bills found");
                        }

                        Contribution firstContribution = contributions.get(0);

                        double totalAmount = req.getAmount();

                        Receipt receipt = new Receipt();

                        receipt.setReceiptNo("CONTR-" + System.currentTimeMillis());
                        receipt.setReceiptDate(LocalDate.now());
                        receipt.setPaymentMode(req.getPaymentMode());
                        receipt.setTransactionId(req.getTransactionId());
                        receipt.setSocietyId(firstContribution.getSociety().getId());
                        receipt.setFlatId(firstContribution.getFlat().getId());
                        receipt.setTotalAmount(totalAmount);
                        receipt.setFinancialYearId(financialYearId);
                        receipt.setStatus(PaymentStatus.SUBMITTED);
                        Receipt savedReceipt = receiptRepository.save(receipt);

                        for (Contribution contribution : contributions) {

                                contribution.setStatus(PaymentStatus.SUBMITTED);
                                contribution.setPaidDate(savedReceipt.getReceiptDate());
                                contribution.setPaymentMode(req.getPaymentMode());
                                contribution.setReceiptId(savedReceipt.getId());
                                contribution.setTransactionId(req.getTransactionId());
                        }

                        contributionRepository.saveAll(contributions);

                        return ResponseEntity.ok(
                                        "Contribution payment recorded successfully");

                } catch (Exception e) {
                        e.printStackTrace();
                        return ResponseEntity.internalServerError()
                                        .body(e.getMessage());
                }
        }
}

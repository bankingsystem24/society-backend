package com.society.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.society.backend.dto.ReceiptRequest;
import com.society.backend.entity.Billing;
import com.society.backend.entity.Flat;
import com.society.backend.entity.Receipt;
import com.society.backend.entity.SinkingFund;
import com.society.backend.enums.PaymentStatus;
import com.society.backend.gl.entity.Contribution;
import com.society.backend.gl.repository.ContributionRepository;
import com.society.backend.gl.service.JournalService;
import com.society.backend.repository.BillingRepository;
import com.society.backend.repository.FlatRepository;
import com.society.backend.repository.ReceiptRepository;
import com.society.backend.repository.SinkingFundRepository;
import com.society.backend.service.ReceiptService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/receipts")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ReceiptController {

        private final ReceiptService receiptService;
        private final ReceiptRepository receiptRepository;
        private final JournalService journalService;
        private final FlatRepository flatRepository;
        private final BillingRepository billingRepository;
        private final SinkingFundRepository sinkingFundRepository;
        private final ContributionRepository contributionRepository;

        @PostMapping("/create")
        public ResponseEntity<?> createReceipt(@RequestBody ReceiptRequest req) {

                Receipt receipt = receiptService.createReceipt(req);

                return ResponseEntity.ok(
                                "Receipt created successfully with No: " + receipt.getReceiptNo());
        }

        @PostMapping("/viewReceipts")
        public ResponseEntity<?> viewReceipts(
                        @RequestBody ReceiptRequest req) {

                return ResponseEntity.ok(
                                receiptService.viewReceipts(
                                                req.getSocietyId(),
                                                req.getFlatId(),
                                                req.getFinancialYearId()));
        }

        @GetMapping("/details/{receiptId}")
        public ResponseEntity<?> getReceiptDetails(
                        @PathVariable Long receiptId) {

                return ResponseEntity.ok(
                                receiptService.getReceiptDetails(receiptId));
        }

        @PutMapping("/confirm")
        public ResponseEntity<?> confirmPayment(
                        @RequestBody Map<String, Object> request) {
                Long receiptId = Long.valueOf(request.get("receiptId").toString());

                String paymentTable = request.get("paymentTable").toString();

                Receipt receipt = receiptRepository.findById(receiptId)
                                .orElseThrow(() -> new RuntimeException("Receipt not found"));
                receipt.setStatus(PaymentStatus.PAID);
                receiptRepository.save(receipt);

                if ("billing".equalsIgnoreCase(paymentTable)) {
                        List<Billing> bills = billingRepository.findByReceiptId(receiptId);
                        bills.forEach(b -> b.setStatus(PaymentStatus.PAID));
                        billingRepository.saveAll(bills);
                } else if ("sinkingfund".equalsIgnoreCase(paymentTable)) {
                        List<SinkingFund> funds = sinkingFundRepository.findByReceiptId(receiptId);
                        funds.forEach(f -> f.setStatus(PaymentStatus.PAID));
                        sinkingFundRepository.saveAll(funds);
                } else if ("contribution".equalsIgnoreCase(paymentTable)){
                        List<Contribution> contribution = contributionRepository.findByReceiptId(receiptId);
                        contribution.forEach(f -> f.setStatus(PaymentStatus.PAID));
                        contributionRepository.saveAll(contribution);
                }

                Flat flat = flatRepository.findById(receipt.getFlatId())
                                .orElseThrow(() -> new RuntimeException("Flat not found"));

                Long memberId = flat.getOwner() != null
                                ? flat.getOwner().getId()
                                : null;

                journalService.createReceiptEntry(
                                receipt.getId(),
                                memberId,
                                receipt.getMaintenanceAmount() != null ? receipt.getMaintenanceAmount() : 0.0,
                                receipt.getInterestAmount() != null ? receipt.getInterestAmount() : 0.0,
                                receipt.getDiscountAmount() != null ? receipt.getDiscountAmount() : 0.0,
                                receipt.getTotalAmount() != null ? receipt.getTotalAmount() : 0.0,
                                receipt.getPaymentMode(),
                                receipt.getSocietyId(),
                                null, // userId if available
                                receipt.getFlatId(),
                                receipt.getFinancialYearId(),
                                Integer.valueOf(request.get("glReceivable").toString()),
                                Integer.valueOf(request.get("glCreditAccount").toString()),
                                Integer.valueOf(request.get("glCashInHand").toString()),
                                Integer.valueOf(request.get("glBankAccount").toString()),
                                Integer.valueOf(request.get("glInterestIncome").toString()),
                                Integer.valueOf(request.get("glDiscount").toString())
                                );

                return ResponseEntity.ok("Payment confirmed successfully");
        }

}
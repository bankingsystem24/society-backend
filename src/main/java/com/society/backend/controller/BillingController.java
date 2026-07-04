package com.society.backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.razorpay.RazorpayClient;
import com.society.backend.dto.BillGenerateRequest;
import com.society.backend.dto.BillingFilterRequest;
import com.society.backend.dto.BillingResponse;
import com.society.backend.dto.ManualPaymentRequest;
import com.society.backend.dto.PaymentRequest;
import com.society.backend.entity.Billing;
import com.society.backend.enums.PaymentStatus;
import com.society.backend.gl.service.BillingService;
import com.society.backend.gl.service.JournalService;
import com.society.backend.repository.BillingRepository;
import com.society.backend.entity.Receipt;
import com.society.backend.repository.ReceiptRepository;
 
@RestController
@RequestMapping("/api/billing")
@CrossOrigin("*")
public class BillingController {
        private final BillingService billingService;
        private final BillingRepository billingRepository;
        private final ReceiptRepository receiptRepository;
        @Value("${razorpay.key_secret}")
        private String razorpayKeySecret;
        @Value("${razorpay.key_id}")
        private String keyId;

        public BillingController(
                        BillingService billingService,
                        RazorpayClient razorpayClient,
                        BillingRepository billingRepository,
                        ReceiptRepository receiptRepository,
                        JournalService journalService,
                        @Value("${razorpay.key_secret}") String razorpayKeySecret,
                        @Value("${razorpay.key_id}") String keyId) {
                this.billingService = billingService;
                this.billingRepository = billingRepository;
                this.receiptRepository = receiptRepository;
                this.razorpayKeySecret = razorpayKeySecret;
                this.keyId = keyId;
        }

        // =========================
        // GENERATE MONTHLY BILLS
        // =========================

        @PostMapping("/generate")
        public String generateBills(
                        @RequestBody BillGenerateRequest request) {

                return billingService.generateMonthlyBills(
                                request.getSocietyId(),
                                request.getMonth(),
                                request.getYear(),
                                request.getCreatedBy(),
                                request.getFinancialYearId(),
                                request.getGlReceivable(),
                                request.getGlCreditAccount());
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
        public ResponseEntity<List<BillingResponse>> viewAllBills(
                        @RequestBody BillingFilterRequest request) {

                return ResponseEntity.ok(
                                billingService.viewAllBills(
                                                request.getSocietyId(),
                                                request.getFlatId(),
                                                request.getFromYear(),
                                                request.getMonth(),
                                                request.getStatus(),
                                                request.getMemberId(),
                                                request.getFinancialYearId()));
        }

        @PutMapping("/pay")
        public String payBills(@RequestBody PaymentRequest req) {

                return billingService.payBills(
                                req.getBillIds(),
                                req.getPaymentMode(),
                                req.getFinancialYearId(),
                                req.getTransactionId(),
                                req.getGlReceivable(),
                                req.getGlCreditAccount(),
                                req.getGlCashInHand(),
                                req.getGlBankAccount(),
                                req.getGlInterestIncome(),
                                req.getGlDiscount(),
                                req.getInterestAmount(),
                                req.getDiscountAmount()
                                );
        }

        @PostMapping("/manual-payment")
        public ResponseEntity<?> manualPayment(
                        @RequestBody ManualPaymentRequest req) {
                
                Double interestAmount = req.getInterestAmount();
                Double discountAmount = req.getDiscountAmount();

                try {
                        Long financialYearId = req.getFinancialYearId();
                        List<Billing> bills = billingRepository.findByIdIn(req.getBillIds());
                        if (bills == null || bills.isEmpty()) {
                                return ResponseEntity.badRequest()
                                                .body("No bills found");
                        }
                        Billing firstBill = bills.get(0);
                        // SocietyBillingPolicy policy = societyBillingPolicyRepository
                        //                 .findBySociety_IdAndFinancialYearId(
                        //                                 firstBill.getSociety().getId(),
                        //                                 financialYearId)
                        //                 .orElse(null);
                        double maintenanceAmount = bills.stream()
                                        .mapToDouble(b -> b.getMaintenanceAmount() != null
                                                        ? b.getMaintenanceAmount()
                                                        : 0.0)
                                        .sum();
                        // double interestAmount = 0.0;
                        // if (policy != null) {

                        //         for (Billing b : bills) {

                        //                 if (b.getDueDate() == null)
                        //                         continue;

                        //                 LocalDate interestStart = b.getDueDate();

                        //                 switch (policy.getInterestType()) {

                        //                         case MONTHLY:
                        //                                 interestStart = interestStart.plusMonths(1);
                        //                                 break;

                        //                         case QUARTERLY:
                        //                                 interestStart = interestStart.plusMonths(3);
                        //                                 break;

                        //                         case HALF_YEARLY:
                        //                                 interestStart = interestStart.plusMonths(6);
                        //                                 break;

                        //                         case YEARLY:
                        //                                 interestStart = interestStart.plusMonths(12);
                        //                                 break;
                        //                 }

                        //                 if (LocalDate.now().isAfter(interestStart)) {

                        //                         long monthsLate = ChronoUnit.MONTHS.between(
                        //                                         interestStart.withDayOfMonth(1),
                        //                                         LocalDate.now().withDayOfMonth(1));

                        //                         monthsLate = Math.max(1, monthsLate);

                        //                         interestAmount += (b.getMaintenanceAmount() != null
                        //                                         ? b.getMaintenanceAmount()
                        //                                         : 0.0)
                        //                                         * policy.getInterestRate()
                        //                                         * monthsLate
                        //                                         / 1200.0;
                        //                 }
                        //         }
                        // }

                        // double discountAmount = bills.stream()
                        //                 .mapToDouble(b -> b.getDiscountAmount() != null
                        //                                 ? b.getDiscountAmount()
                        //                                 : 0.0)
                        //                 .sum();

                        double totalAmount = maintenanceAmount + interestAmount- discountAmount;

                        Receipt receipt = new Receipt();
                        receipt.setReceiptNo("RCPT-" + System.currentTimeMillis());
                        receipt.setReceiptDate(LocalDate.now());
                        receipt.setPaymentMode(req.getPaymentMode());
                        // UTR Number
                        receipt.setTransactionId(req.getTransactionId());
                        receipt.setSocietyId(firstBill.getSociety().getId());
                        receipt.setFlatId(firstBill.getFlat().getId());
                        receipt.setMaintenanceAmount(maintenanceAmount);
                        receipt.setInterestAmount(interestAmount);
                        receipt.setDiscountAmount(discountAmount);
                        receipt.setTotalAmount(totalAmount);
                        receipt.setFinancialYearId(financialYearId);
                        receipt.setStatus(PaymentStatus.SUBMITTED);

                        Receipt savedReceipt = receiptRepository.save(receipt);

                        for (Billing bill : bills) {

                                bill.setStatus(PaymentStatus.SUBMITTED);
                                bill.setPaidDate(LocalDate.now());
                                bill.setPaymentMode(req.getPaymentMode());
                                bill.setReceiptId(savedReceipt.getId());
                                bill.setTransactionId(req.getTransactionId());
                        }

                        billingRepository.saveAll(bills);

                        return ResponseEntity.ok(
                                        "Payment recorded successfully");

                } catch (Exception e) {

                        e.printStackTrace();

                        return ResponseEntity.internalServerError()
                                        .body(e.getMessage());
                }
        }

}
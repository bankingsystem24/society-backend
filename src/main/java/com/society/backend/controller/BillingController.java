package com.society.backend.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.razorpay.RazorpayClient;
import com.society.backend.dto.BillGenerateRequest;
import com.society.backend.dto.BillingFilterRequest;
import com.society.backend.dto.BillingResponse;
import com.society.backend.dto.InterestCalculationRequest;
import com.society.backend.dto.InterestCalculationResponse;
import com.society.backend.dto.ManualPaymentRequest;
import com.society.backend.dto.PaymentRequest;
import com.society.backend.dto.PostArrearsRequest;
import com.society.backend.entity.Billing;
import com.society.backend.enums.PaymentStatus;
import com.society.backend.gl.dto.ArrearsResponse;
import com.society.backend.gl.entity.DiscountPolicy;
import com.society.backend.gl.entity.SocietyBillingPolicy;
import com.society.backend.gl.repository.DiscountPolicyRepository;
import com.society.backend.gl.repository.SocietyBillingPolicyRepository;
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
        private final SocietyBillingPolicyRepository societyBillingPolicyRepository;
        private final DiscountPolicyRepository discountPolicyRepository;
        @Value("${razorpay.key_secret}")
        private String razorpayKeySecret;
        @Value("${razorpay.key_id}")
        private String keyId;

        public BillingController(
                        BillingService billingService,
                        RazorpayClient razorpayClient,
                        BillingRepository billingRepository,
                        ReceiptRepository receiptRepository,
                        SocietyBillingPolicyRepository societyBillingPolicyRepository,
                        DiscountPolicyRepository discountPolicyRepository,
                        JournalService journalService,
                        @Value("${razorpay.key_secret}") String razorpayKeySecret,
                        @Value("${razorpay.key_id}") String keyId) {
                this.billingService = billingService;
                this.billingRepository = billingRepository;
                this.receiptRepository = receiptRepository;
                this.societyBillingPolicyRepository = societyBillingPolicyRepository;
                this.discountPolicyRepository = discountPolicyRepository;
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
                                req.getPaymentDate(),
                                req.getFinancialYearId(),
                                req.getTransactionId(),
                                req.getGlReceivable(),
                                req.getGlCreditAccount(),
                                req.getGlCashInHand(),
                                req.getGlBankAccount(),
                                req.getGlInterestIncome(),
                                req.getGlDiscount(),
                                req.getInterestAmount(),
                                req.getDiscountAmount(),
                                req.getSelectedCount()
                                );
        }

        @PostMapping("/member/manual-payment") 
        public ResponseEntity<?> manualPayment(@RequestBody ManualPaymentRequest req) 
        {
                Double discountAmount = req.getDiscountAmount();

                try {
                        Long financialYearId = req.getFinancialYearId();
                        List<Billing> bills = billingRepository.findByIdIn(req.getBillIds());
                        if (bills == null || bills.isEmpty()) {
                                return ResponseEntity.badRequest()
                                                .body("No bills found");
                        }
                        Billing firstBill = bills.get(0);
                        
                        SocietyBillingPolicy policy = societyBillingPolicyRepository
                                        .findBySociety_IdAndFinancialYearId(
                                                        firstBill.getSociety().getId(),
                                                        financialYearId)
                                        .orElse(null);
                                        
                        double maintenanceAmount = bills.stream()
                                        .mapToDouble(b -> b.getMaintenanceAmount() != null
                                                        ? b.getMaintenanceAmount()
                                                        : 0.0)
                                        .sum();
                        double interestAmount = 0.0;
                        if (policy != null) {

                                for (Billing b : bills) {

                                        if (b.getDueDate() == null)
                                                continue;

                                        LocalDate interestStart = b.getCreatedDate();

                                        switch (policy.getInterestType()) {

                                                case MONTHLY:
                                                        interestStart = interestStart.plusMonths(1);
                                                        break;

                                                case QUARTERLY:
                                                        interestStart = interestStart.plusMonths(3);
                                                        break;

                                                case HALF_YEARLY:
                                                        interestStart = interestStart.plusMonths(6);
                                                        break;

                                                case YEARLY:
                                                        interestStart = interestStart.plusMonths(12);
                                                        break;
                                        }

                                        if (LocalDate.now().isAfter(interestStart)) {

                                                long daysLate = ChronoUnit.DAYS.between(
                                                        b.getCreatedDate(),
                                                        LocalDate.now());

                                        daysLate = Math.max(0, daysLate);

                                        // Annual interest rate converted to daily interest
                                        interestAmount = b.getMaintenanceAmount()
                                                        * policy.getInterestRate()
                                                        * daysLate
                                                        / (365.0 * 100.0);
                                        }

                                        
                                }
                                interestAmount = Math.round(interestAmount);
                        }


                       List<DiscountPolicy> policies = discountPolicyRepository
                                .findBySociety_IdAndActiveTrue(req.getSocietyId());

                        DiscountPolicy discountPolicy = policies.isEmpty() ? null : policies.get(0);

                        BigDecimal discount = BigDecimal.ZERO;

                        if (discountPolicy != null && req.getSelectedCount() == 12
                                && LocalDate.now().isBefore(discountPolicy.getPaidBeforeDate())) {

                        
                                discount = BigDecimal.valueOf(maintenanceAmount)
                                                .multiply(discountPolicy.getDiscountPercent())
                                                .divide(BigDecimal.valueOf(100));
                                
                        
                        }

                        discountAmount = discount.doubleValue();


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

                        return ResponseEntity.ok("Payment recorded successfully");

                } catch (Exception e) {

                        e.printStackTrace();

                        return ResponseEntity.internalServerError()
                                        .body(e.getMessage());
                }
        }

        @PostMapping("/calculate-interest")
                public ResponseEntity<InterestCalculationResponse> calculateInterest(
                        @RequestBody InterestCalculationRequest request) {

                return ResponseEntity.ok(billingService.calculateInterest(request)
                );
        }

        @PostMapping("/calculate-discount")
        public ResponseEntity<InterestCalculationResponse> calculateDiscount(
                        @RequestBody InterestCalculationRequest request) {

                return ResponseEntity.ok(
                        billingService.calculateDiscount(request));
        }

        @PostMapping("/generate-financial-year-bills")
        public ResponseEntity<?> generateFinancialYearBills(
                @RequestBody BillGenerateRequest request) {

        billingService.generateFinancialYearBills(request);

        return ResponseEntity.ok("Bills generated successfully.");
        }

        @PostMapping("/arrears")
        public ResponseEntity<?> postArrears(@RequestBody PostArrearsRequest request) 
        {
                billingService.saveArrears(request);
                return ResponseEntity.ok("Arrears Saved Successfully");
        }

        @GetMapping("/arrears")
        public ResponseEntity<List<ArrearsResponse>> getArrears(
                @RequestParam Long societyId,
                @RequestParam Long financialYearId) {

        return ResponseEntity.ok(
                billingService.getArrears(societyId, financialYearId));
        }  
        
        @DeleteMapping("/{id}")
        public ResponseEntity<String> deleteBill(@PathVariable Long id) {

        billingService.deleteBill(id);

        return ResponseEntity.ok("Bill deleted successfully");
        }

}
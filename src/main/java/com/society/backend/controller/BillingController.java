package com.society.backend.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.society.backend.dto.BillGenerateRequest;
import com.society.backend.dto.BillingFilterRequest;
import com.society.backend.dto.BillingResponse;
import com.society.backend.dto.CreateOrderRequest;
import com.society.backend.dto.ManualPaymentRequest;
import com.society.backend.dto.PaymentRequest;
import com.society.backend.dto.VerifyPaymentRequest;
import com.society.backend.entity.Billing;
import com.society.backend.enums.PaymentStatus;
import com.society.backend.gl.entity.SocietyBillingPolicy;
import com.society.backend.gl.repository.SocietyBillingPolicyRepository;
import com.society.backend.gl.service.BillingService;
import com.society.backend.gl.service.JournalService;
import com.society.backend.repository.BillingRepository;
import com.society.backend.entity.Receipt;
import com.society.backend.repository.ReceiptRepository;
import com.society.backend.util.RazorpaySignatureUtil;

@RestController
@RequestMapping("/api/billing")
@CrossOrigin("*")
public class BillingController {
        private final BillingService billingService;
        private final RazorpayClient razorpayClient;
        private final BillingRepository billingRepository;
        private final ReceiptRepository receiptRepository;
        private final JournalService journalService;
        private final SocietyBillingPolicyRepository societyBillingPolicyRepository;
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
                        SocietyBillingPolicyRepository societyBillingPolicyRepository,
                        @Value("${razorpay.key_secret}") String razorpayKeySecret,
                        @Value("${razorpay.key_id}") String keyId) {
                this.billingService = billingService;
                this.razorpayClient = razorpayClient;
                this.billingRepository = billingRepository;
                this.receiptRepository = receiptRepository;
                this.journalService = journalService;
                this.societyBillingPolicyRepository = societyBillingPolicyRepository;
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
                                req.getGlDiscount()
                                );
        }

        // @PostMapping("/create-order")
        // public Map<String, Object> createOrder(@RequestBody CreateOrderRequest req) throws Exception {

        //         // 1️⃣ Convert amount to paise
        //         int amountInPaise = (int) (req.getAmount() * 100);

        //         // 2️⃣ Create Razorpay order JSON
        //         JSONObject orderRequest = new JSONObject();
        //         orderRequest.put("amount", amountInPaise);
        //         orderRequest.put("currency", "INR");
        //         orderRequest.put("receipt", "rcpt_" + System.currentTimeMillis());
        //         orderRequest.put("payment_capture", 1);

        //         // 3️⃣ Create order
        //         Order order = razorpayClient.orders.create(orderRequest);

        //         // 4️⃣ Response to frontend
        //         Map<String, Object> response = new HashMap<>();
        //         response.put("razorpayOrderId", order.get("id"));
        //         response.put("amount", amountInPaise);
        //         response.put("key", keyId);

        //         return response;
        // }

        // @PostMapping("/verify-payment")
        // public ResponseEntity<?> verifyPayment(@RequestBody VerifyPaymentRequest req) {

        //         try {

        //                 // =========================
        //                 // VERIFY SIGNATURE
        //                 // =========================
        //                 String payload = req.getRazorpayOrderId() + "|" + req.getRazorpayPaymentId();
        //                 Long financialYearId = req.getFinancialYearId();

        //                 String generatedSignature = RazorpaySignatureUtil.hmacSHA256(payload, razorpayKeySecret);

        //                 if (!generatedSignature.equals(req.getRazorpaySignature())) {
        //                         return ResponseEntity.badRequest().body("Invalid payment signature");
        //                 }

        //                 // =========================
        //                 // FETCH BILLS
        //                 // =========================
        //                 List<Billing> bills = billingRepository.findByIdIn(req.getBillIds());

        //                 if (bills == null || bills.isEmpty()) {
        //                         return ResponseEntity.badRequest().body("No bills found");
        //                 }

        //                 Billing firstBill = bills.get(0);

        //                 // =========================
        //                 // LOAD POLICY
        //                 // =========================
        //                 SocietyBillingPolicy policy = societyBillingPolicyRepository
        //                                 .findBySociety_IdAndFinancialYearId(firstBill.getSociety().getId(),
        //                                                 financialYearId)
        //                                 .orElse(null);

        //                 // =========================
        //                 // CALCULATE AMOUNTS
        //                 // =========================
        //                 double maintenanceAmount = bills.stream()
        //                                 .mapToDouble(b -> b.getMaintenanceAmount() != null ? b.getMaintenanceAmount() : 0.0)
        //                                 .sum();
        //                 double interestAmount = 0.0;
        //                 if (policy != null) {
        //                         for (Billing b : bills) {
        //                                 if (b.getDueDate() == null)
        //                                         continue;
        //                                 LocalDate interestStart = b.getDueDate();
        //                                 switch (policy.getInterestType()) {
        //                                         case MONTHLY:
        //                                                 interestStart = interestStart.plusMonths(1);
        //                                                 break;
        //                                         case QUARTERLY:
        //                                                 interestStart = interestStart.plusMonths(3);
        //                                                 break;
        //                                         case HALF_YEARLY:
        //                                                 interestStart = interestStart.plusMonths(6);
        //                                                 break;
        //                                         case YEARLY:
        //                                                 interestStart = interestStart.plusMonths(12);
        //                                                 break;
        //                                 }
        //                                 if (LocalDate.now().isAfter(interestStart)) {
        //                                         long monthsLate = ChronoUnit.MONTHS.between(
        //                                                         interestStart.withDayOfMonth(1),
        //                                                         LocalDate.now().withDayOfMonth(1));
        //                                         monthsLate = Math.max(1, monthsLate);
        //                                         interestAmount += (b.getMaintenanceAmount() != null
        //                                                         ? b.getMaintenanceAmount()
        //                                                         : 0.0)
        //                                                         * policy.getInterestRate()
        //                                                         * monthsLate
        //                                                         / 1200.0;
        //                                 }
        //                         }
        //                 }

        //                 double discountAmount = bills.stream()
        //                                 .mapToDouble(b -> b.getDiscountAmount() != null ? b.getDiscountAmount() : 0.0)
        //                                 .sum();
        //                 double totalAmount = maintenanceAmount + interestAmount - discountAmount;

        //                 // =========================
        //                 // CREATE RECEIPT
        //                 // =========================
        //                 Receipt receipt = new Receipt();

        //                 receipt.setReceiptNo("RCPT-" + System.currentTimeMillis());
        //                 receipt.setReceiptDate(LocalDate.now());
        //                 receipt.setPaymentMode("ONLINE");
        //                 receipt.setTransactionId(req.getRazorpayPaymentId());

        //                 receipt.setSocietyId(firstBill.getSociety().getId());
        //                 receipt.setFlatId(firstBill.getFlat().getId());

        //                 receipt.setMaintenanceAmount(maintenanceAmount);
        //                 receipt.setInterestAmount(interestAmount);
        //                 receipt.setDiscountAmount(discountAmount);
        //                 receipt.setTotalAmount(totalAmount);
        //                 receipt.setFinancialYearId(financialYearId);
        //                 receipt.setStatus(PaymentStatus.PAID);

        //                 Receipt savedReceipt = receiptRepository.save(receipt);
        //                 // =========================
        //                 // UPDATE BILLS
        //                 // =========================
        //                 for (Billing bill : bills) {
        //                         bill.setStatus(PaymentStatus.PAID);
        //                         bill.setPaidDate(LocalDate.now());
        //                         bill.setPaymentMode("ONLINE");
        //                         bill.setReceiptId(savedReceipt.getId());
        //                         double maintenance = bill.getMaintenanceAmount() != null ? bill.getMaintenanceAmount()
        //                                         : 0.0;
        //                         double discount = bill.getDiscountAmount() != null ? bill.getDiscountAmount() : 0.0;
        //                         double interest = 0.0;
        //                         if (policy != null && bill.getDueDate() != null) {
        //                                 LocalDate interestStart = bill.getDueDate();
        //                                 switch (policy.getInterestType()) {
        //                                         case MONTHLY:
        //                                                 interestStart = interestStart.plusMonths(1);
        //                                                 break;
        //                                         case QUARTERLY:
        //                                                 interestStart = interestStart.plusMonths(3);
        //                                                 break;
        //                                         case HALF_YEARLY:
        //                                                 interestStart = interestStart.plusMonths(6);
        //                                                 break;
        //                                         case YEARLY:
        //                                                 interestStart = interestStart.plusMonths(12);
        //                                                 break;
        //                                 }
        //                                 if (LocalDate.now().isAfter(interestStart)) {

        //                                         long monthsLate = ChronoUnit.MONTHS.between(
        //                                                         interestStart.withDayOfMonth(1),
        //                                                         LocalDate.now().withDayOfMonth(1));

        //                                         monthsLate = Math.max(1, monthsLate);

        //                                         interest = maintenance
        //                                                         * policy.getInterestRate()
        //                                                         * monthsLate
        //                                                         / 1200.0;
        //                                 }
        //                         }
        //                         bill.setInterestAmount(interest);
        //                         double total = maintenance + interest - discount;
        //                         bill.setTotalAmount(total);
        //                         bill.setTransactionId(req.getRazorpayPaymentId());
        //                 }

        //                 billingRepository.saveAll(bills);

        //                 // =========================
        //                 // JOURNAL ENTRY
        //                 // =========================
        //                 Long memberId = firstBill.getFlat().getOwner() != null
        //                                 ? firstBill.getFlat().getOwner().getId()
        //                                 : null;

        //                 journalService.createReceiptEntry(
        //                                 savedReceipt.getId(),
        //                                 memberId,
        //                                 maintenanceAmount,
        //                                 interestAmount,
        //                                 discountAmount,
        //                                 totalAmount,
        //                                 "ONLINE",
        //                                 firstBill.getSociety().getId(),
        //                                 req.getUserId(),
        //                                 firstBill.getFlat().getId(),
        //                                 financialYearId);

        //                 return ResponseEntity.ok("Payment verified successfully");

        //         } catch (Exception e) {

        //                 e.printStackTrace();

        //                 return ResponseEntity.internalServerError()
        //                                 .body(e.getMessage());
        //         }
        // }

        @PostMapping("/manual-payment")
        public ResponseEntity<?> manualPayment(
                        @RequestBody ManualPaymentRequest req) {

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

                                        LocalDate interestStart = b.getDueDate();

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

                                                long monthsLate = ChronoUnit.MONTHS.between(
                                                                interestStart.withDayOfMonth(1),
                                                                LocalDate.now().withDayOfMonth(1));

                                                monthsLate = Math.max(1, monthsLate);

                                                interestAmount += (b.getMaintenanceAmount() != null
                                                                ? b.getMaintenanceAmount()
                                                                : 0.0)
                                                                * policy.getInterestRate()
                                                                * monthsLate
                                                                / 1200.0;
                                        }
                                }
                        }

                        double discountAmount = bills.stream()
                                        .mapToDouble(b -> b.getDiscountAmount() != null
                                                        ? b.getDiscountAmount()
                                                        : 0.0)
                                        .sum();

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
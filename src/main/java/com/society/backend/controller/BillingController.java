package com.society.backend.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import com.society.backend.dto.BillGenerateRequest;
import com.society.backend.dto.BillingFilterRequest;
import com.society.backend.dto.BillingResponse;
import com.society.backend.dto.CreateOrderRequest;
import com.society.backend.dto.PaymentRequest;
import com.society.backend.dto.VerifyPaymentRequest;
import com.society.backend.entity.Billing;
import com.society.backend.enums.PaymentStatus;
import com.society.backend.gl.service.BillingService;
import com.society.backend.repository.BillingRepository;
import com.society.backend.entity.Receipt;
import com.society.backend.repository.ReceiptRepository;

@RestController
@RequestMapping("/api/billing")
@CrossOrigin("*")
public class BillingController {

    @Autowired
    private BillingService billingService;

    private final RazorpayClient razorpayClient;

    private final BillingRepository billingRepository;

    private final ReceiptRepository receiptRepository;

   @Value("${razorpay.key_secret}")
   private String razorpayKeySecret;

    @Value("${razorpay.key_id}")
    private String keyId;

public BillingController(
        RazorpayClient razorpayClient,
        BillingRepository billingRepository,
        ReceiptRepository receiptRepository
) {
    this.razorpayClient = razorpayClient;
    this.billingRepository = billingRepository;
    this.receiptRepository = receiptRepository;
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
public ResponseEntity<List<BillingResponse>> viewAllBills(
        @RequestBody BillingFilterRequest request) {

    return ResponseEntity.ok(
            billingService.viewAllBills(
                    request.getSocietyId(),
                    request.getFlatId(),
                    request.getFromYear(),
                    request.getMonth(),
                    request.getStatus(),
                    request.getMemberId()
            )
    );
}

        @PutMapping("/pay")
        public String payBills(@RequestBody PaymentRequest req) {

        return billingService.payBills(
                req.getBillIds(),
                req.getPaymentMode()
        );
        }

         @PostMapping("/create-order")
        public Map<String, Object> createOrder(@RequestBody CreateOrderRequest req) throws Exception {

                // 1️⃣ Convert amount to paise
                int amountInPaise = (int) (req.getAmount() * 100);

                // 2️⃣ Create Razorpay order JSON
                JSONObject orderRequest = new JSONObject();
                orderRequest.put("amount", amountInPaise);
                orderRequest.put("currency", "INR");
                orderRequest.put("receipt", "rcpt_" + System.currentTimeMillis());
                orderRequest.put("payment_capture", 1);

                // 3️⃣ Create order
                Order order = razorpayClient.orders.create(orderRequest);

                // 4️⃣ Response to frontend
                Map<String, Object> response = new HashMap<>();
                response.put("razorpayOrderId", order.get("id"));
                response.put("amount", amountInPaise);
                response.put("key", keyId);

                return response;
        }

        @PostMapping("/verify-payment")
        public ResponseEntity<?> verifyPayment(
                @RequestBody VerifyPaymentRequest req
        ) {

        try {

                JSONObject options = new JSONObject();

                options.put(
                        "razorpay_order_id",
                        req.getRazorpayOrderId()
                );

                options.put(
                        "razorpay_payment_id",
                        req.getRazorpayPaymentId()
                );

                options.put(
                        "razorpay_signature",
                        req.getRazorpaySignature()
                );

                // =========================
                // VERIFY SIGNATURE
                // =========================

                boolean isValid =
                        Utils.verifyPaymentSignature(
                                options,
                                razorpayKeySecret
                        );

                if (!isValid) {

                return ResponseEntity.badRequest()
                        .body("Invalid payment signature");
                }

                List<Billing> bills =
                billingRepository.findByIdIn(
                        req.getBillIds()
                );

                Billing firstBill = bills.get(0);
                // =========================
                // CREATE RECEIPT
                // =========================

                Receipt receipt = new Receipt();

                // =========================
                // BASIC DETAILS
                // =========================

                receipt.setPaymentMode(
                        req.getPaymentMode()
                );

                receipt.setTransactionId(
                        req.getRazorpayPaymentId()
                );

                receipt.setReceiptDate(
                        LocalDate.now()
                );

                receipt.setTotalAmount(
                        req.getAmount()
                );

                // =========================
                // RECEIPT NUMBER
                // =========================

                receipt.setReceiptNo(
                        "RCPT-" + System.currentTimeMillis()
                );

                // =========================
                // SOCIETY + FLAT
                // =========================

                receipt.setSocietyId(
                        firstBill.getSociety().getId()
                );

                receipt.setFlatId(
                        firstBill.getFlat().getId()
                );

                // =========================
                // SAVE RECEIPT
                // =========================

                receipt = receiptRepository.save(receipt);

                billingRepository.updateReceiptAndStatus(
                        receipt.getId(),
                        PaymentStatus.PAID,
                        req.getRazorpayPaymentId(),
                        req.getPaymentMode(),   // 👈 ADD THIS
                        req.getBillIds()
                );


                return ResponseEntity.ok(
                        "Payment verified successfully"
                );

        } catch (Exception e) {

                e.printStackTrace();

                return ResponseEntity.internalServerError()
                        .body(e.toString());
        }
        }

}
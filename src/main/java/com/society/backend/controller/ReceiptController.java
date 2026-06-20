package com.society.backend.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.society.backend.dto.ReceiptRequest;
import com.society.backend.entity.Receipt;
import com.society.backend.enums.PaymentStatus;
import com.society.backend.repository.ReceiptRepository;
import com.society.backend.service.ReceiptService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/receipts")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ReceiptController {

    private final ReceiptService receiptService;
    private final ReceiptRepository receiptRepository;

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
            System.out.println("Payment Table: " + paymentTable);
        return ResponseEntity.ok("Payment confirmed successfully");
    }

}
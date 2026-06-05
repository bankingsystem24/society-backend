package com.society.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.society.backend.dto.ReceiptRequest;
import com.society.backend.entity.Receipt;
import com.society.backend.service.ReceiptService;

@RestController
@RequestMapping("/api/receipts")
@CrossOrigin("*")
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @PostMapping("/create")
    public ResponseEntity<?> createReceipt(@RequestBody ReceiptRequest req) {

        Receipt receipt = receiptService.createReceipt(req);

        return ResponseEntity.ok(
            "Receipt created successfully with No: " + receipt.getReceiptNo()
        );
    }

    @PostMapping("/viewReceipts")
    public ResponseEntity<?> viewReceipts(
            @RequestBody ReceiptRequest req
    ) {

        return ResponseEntity.ok(
                receiptService.viewReceipts(
                        req.getSocietyId(),
                        req.getFlatId()
                )
        );
    }

    @GetMapping("/details/{receiptId}")
    public ResponseEntity<?> getReceiptDetails(
            @PathVariable Long receiptId
    ) {

        return ResponseEntity.ok(
                receiptService.getReceiptDetails(receiptId)
        );
    }

    
}
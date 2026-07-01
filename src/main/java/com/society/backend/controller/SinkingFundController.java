package com.society.backend.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.society.backend.dto.SinkingFundPaymentRequest;
import com.society.backend.dto.SinkingFundRequest;
import com.society.backend.dto.SinkingFundResponse;
import com.society.backend.entity.SinkingFund;
import com.society.backend.service.SinkingFundService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/sinking-fund")
@RequiredArgsConstructor
@CrossOrigin
public class SinkingFundController {

    private final SinkingFundService sinkingFundService;


    // CREATE / GENERATE SINGLE RECORD
    @PostMapping("/generate") 
    public void generate(@RequestBody SinkingFundRequest request) {
        sinkingFundService.generate(
                request.getSocietyId(),
                request.getMonth(),
                request.getYear(),
                request.getAmount(),
                request.getCreatedBy(),
                request.getFinancialYearId(),
                request.getGlReceivable(),
                request.getGlCreditAccount()
                );
    }

    // GET ALL
    @GetMapping
    public List<SinkingFundResponse> getAll(@RequestParam Long societyId) {
        return sinkingFundService.getAll(societyId);
    }

    // GET BY FLAT (VERY USEFUL)
    @GetMapping("/flat")
    public List<SinkingFund> getByFlat(
            @RequestParam Long societyId,
            @RequestParam Long flatId) {
        return sinkingFundService.getByFlat(societyId, flatId);
    }

    @PutMapping("/pay")
    public ResponseEntity<String> pay(
            @RequestBody SinkingFundPaymentRequest request) {

        sinkingFundService.pay(
                request.getSinkingFundIds(),
                request.getPaymentMode(),
                request.getFinancialYearId(),
                request.getGlReceivable(),
                request.getGlCreditAccount(),
                request.getGlCashInHand(),
                request.getGlBankAccount(),
                request.getGlInterestIncome(),
                request.getGlDiscount(),
                request.getTransactionId()

                 );

        return ResponseEntity.ok("Payment successful");
    }

    // @PostMapping("/create-order")
    // public ResponseEntity<?> createOrder(
    //         @RequestBody SinkingFundOrderRequest request) {

    //     return ResponseEntity.ok(
    //             sinkingFundService.createOrder(request));
    // }

    // @PostMapping("/verify-payment")
    // public ResponseEntity<?> verifyPayment(
    //         @RequestBody VerifySinkingFundPaymentRequest request) {

    //     sinkingFundService.verifyPayment(request);

    //     return ResponseEntity.ok("Payment verified successfully");
    // }
}
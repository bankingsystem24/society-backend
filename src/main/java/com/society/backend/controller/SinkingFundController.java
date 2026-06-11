package com.society.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.society.backend.dto.SinkingFundPaymentRequest;
import com.society.backend.dto.SinkingFundRequest;
import com.society.backend.dto.SinkingFundResponse;
import com.society.backend.entity.SinkingFund;
import com.society.backend.service.SinkingFundService;

import java.util.List;

@RestController
@RequestMapping("/api/sinking-fund")
@CrossOrigin
public class SinkingFundController {

    private final SinkingFundService sinkingFundService;

    public SinkingFundController(SinkingFundService sinkingFundService){
        this.sinkingFundService = sinkingFundService;
    };

    // CREATE / GENERATE SINGLE RECORD
@PostMapping("/generate")
public void generate(@RequestBody SinkingFundRequest request) {
    sinkingFundService.generate(
        request.getSocietyId(),
        request.getMonth(),
        request.getYear(),
        request.getAmount(),
        request.getCreatedBy()
    );
}

    // BULK GENERATE (for all flats of society - VERY IMPORTANT for your case)
    // @PostMapping("/generate-all")
    // public String generateForAllFlats(
    //         @RequestParam Long societyId,
    //         @RequestParam String month,
    //         @RequestParam int year,
    //         @RequestParam Double amount,
    //         @RequestParam Long createdBy
    // ) {
    //     service.generateForAllFlats(societyId, month, year, amount, createdBy);
    //     return "Sinking Fund generated for all flats";
    // }

    // GET ALL
    @GetMapping
    public List<SinkingFundResponse> getAll(@RequestParam Long societyId) {
        return sinkingFundService.getAll(societyId);
    }

    // GET BY FLAT (VERY USEFUL)
    @GetMapping("/flat")
    public List<SinkingFund> getByFlat(
            @RequestParam Long societyId,
            @RequestParam Long flatId
    ) {
        return sinkingFundService.getByFlat(societyId, flatId);
    }

    @PutMapping("/pay")
    public ResponseEntity<String> pay(
            @RequestBody SinkingFundPaymentRequest request) {

        sinkingFundService.pay(
                request.getSinkingFundIds(),
                request.getPaymentMode());

        return ResponseEntity.ok("Payment successful");
    }
}
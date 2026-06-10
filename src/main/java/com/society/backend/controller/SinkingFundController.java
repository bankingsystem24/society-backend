package com.society.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.society.backend.dto.SinkingFundRequest;
import com.society.backend.dto.SinkingFundResponse;
import com.society.backend.entity.SinkingFund;
import com.society.backend.service.SinkingFundService;

import java.util.List;

@RestController
@RequestMapping("/api/sinking-fund")
@CrossOrigin
public class SinkingFundController {

    @Autowired
    private SinkingFundService service;

    // CREATE / GENERATE SINGLE RECORD
@PostMapping("/generate")
public void generate(@RequestBody SinkingFundRequest request) {
    service.generate(
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
        return service.getAll(societyId);
    }

    // GET BY FLAT (VERY USEFUL)
    @GetMapping("/flat")
    public List<SinkingFund> getByFlat(
            @RequestParam Long societyId,
            @RequestParam Long flatId
    ) {
        return service.getByFlat(societyId, flatId);
    }
}
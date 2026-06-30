package com.society.backend.gl.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.society.backend.gl.dto.IncomeVoucherRequest;
import com.society.backend.gl.entity.IncomeVoucher;
import com.society.backend.gl.service.IncomeVoucherService;

@RestController
@RequestMapping("/api/income")
@CrossOrigin(origins = "*")
public class IncomeVoucherController {

    private final IncomeVoucherService incomeVoucherService;

    public IncomeVoucherController(IncomeVoucherService incomeVoucherService) {
        this.incomeVoucherService = incomeVoucherService;
    }

    // Save Income Voucher
    @PostMapping
    public ResponseEntity<IncomeVoucher> save(
            @RequestBody IncomeVoucherRequest request) {

        return ResponseEntity.ok(incomeVoucherService.save(request));
    }

    // Get All Income Vouchers
    @GetMapping("/{societyId}/{financialYearId}")
    public ResponseEntity<List<IncomeVoucher>> getAll(
            @PathVariable Long societyId,
            @PathVariable Long financialYearId) {

        return ResponseEntity.ok(
                incomeVoucherService.getAll(societyId, financialYearId));
    }

    // Get By Id
    @GetMapping("/{id}")
    public ResponseEntity<IncomeVoucher> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                incomeVoucherService.getById(id));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id) {

        incomeVoucherService.delete(id);

        return ResponseEntity.ok("Income voucher deleted successfully.");
    }

}

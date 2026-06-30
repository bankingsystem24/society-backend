package com.society.backend.gl.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.society.backend.gl.dto.ExpenseVoucherRequest;
import com.society.backend.gl.entity.ExpenseVoucher;
import com.society.backend.gl.service.ExpenseVoucherService;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")
public class ExpenseVoucherController {

    private final ExpenseVoucherService expenseVoucherService;

    public ExpenseVoucherController(
            ExpenseVoucherService expenseVoucherService) {
        this.expenseVoucherService = expenseVoucherService;
    }

    // CREATE
    @PostMapping
    public ExpenseVoucher save(
            @RequestBody ExpenseVoucherRequest request) {
        return expenseVoucherService.save(request);
    }

    // LIST BY SOCIETY
    @GetMapping("/{societyId}/{financialYearId}")
    public List<ExpenseVoucher> getBySocietyIdAndFinancialYearId(
            @PathVariable Long societyId,
            @PathVariable Long financialYearId) {

        return expenseVoucherService.getBySocietyIdAndFinancialYearId(societyId,financialYearId);
    }

    // GET SINGLE
    @GetMapping("/details/{id}")
    public ExpenseVoucher getOne(
            @PathVariable Long id) {

        return expenseVoucherService.getById(id);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id) {
        expenseVoucherService.delete(id);
    }
}
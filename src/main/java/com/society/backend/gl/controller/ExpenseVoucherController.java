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
    @GetMapping("/{societyId}")
    public List<ExpenseVoucher> getBySociety(
            @PathVariable Long societyId) {

        return expenseVoucherService.getBySociety(societyId);
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
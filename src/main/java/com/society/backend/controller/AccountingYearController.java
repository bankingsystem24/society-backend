package com.society.backend.controller;

import com.society.backend.entity.AccountingYear;
import com.society.backend.gl.dto.CloseAccountingYearRequest;
import com.society.backend.security.JwtUtil;
import com.society.backend.service.AccountingYearService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounting-year")
@CrossOrigin(origins = "*")
public class AccountingYearController {

    private final AccountingYearService accountingYearService;

    public AccountingYearController(AccountingYearService accountingYearService,
    JwtUtil jwtUtil){
        this.accountingYearService = accountingYearService;
    };

    @GetMapping("")
    public List<AccountingYear> getAll() {
            return accountingYearService.getAllYears();
    }

    // =====================================================
    // GET ALL YEARS BY SOCIETY
    // =====================================================
    @GetMapping("/{societyId}")
    public List<AccountingYear> getAll(@PathVariable Long societyId) {

        try {
            return accountingYearService.getAllYears(societyId);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // =====================================================
    // GET ACTIVE YEAR
    // =====================================================
    @GetMapping("/{societyId}/active")
    public AccountingYear getActive(@PathVariable Long societyId) {
        
        return accountingYearService.getActiveYear(societyId);
    }

    // =====================================================
    // CREATE FINANCIAL YEAR
    // =====================================================
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Map<String, Object> req) {

        Long societyId = Long.valueOf(req.get("societyId").toString());
        String fyCode = req.get("fyCode").toString();
        String username = req.get("username").toString();
        LocalDate startDate = LocalDate.parse(req.get("startDate").toString());
        LocalDate endDate = LocalDate.parse(req.get("endDate").toString());

        AccountingYear year = accountingYearService.createYear(
                societyId,
                fyCode,
                startDate,
                endDate,
                username
        );

        return ResponseEntity.ok(Map.of(
                "success", true,
                "id", year.getId(),
                "fyCode", year.getFyCode()
        ));
    }

    // =====================================================
    // SET ACTIVE YEAR
    // =====================================================
    @PutMapping("/activate")
    public String activate(@RequestBody Map<String, Object> req) {

        Long societyId = Long.valueOf(req.get("societyId").toString());
        Long yearId = Long.valueOf(req.get("yearId").toString());

       
        return accountingYearService.setActiveYear(societyId, yearId);
    }

    @PostMapping("/close")
    public ResponseEntity<String> closeAccountingYear(
            @RequestBody CloseAccountingYearRequest request) {

        String message = accountingYearService.closeAccountingYear(
                request.getAccountingYearId(),
                request.getSocietyId(),
                request.getUsername()
        );

        return ResponseEntity.ok(message);
    }

    @PostMapping("/open")
    public ResponseEntity<String> openAccountingYear(
            @RequestBody CloseAccountingYearRequest request) {

        String message = accountingYearService.openAccountingYear(
                request.getAccountingYearId(),
                request.getSocietyId(),
                request.getUsername()
        );

        return ResponseEntity.ok(message);
    }

    @PostMapping("/year-end-close")
    public ResponseEntity<String> yearEndClose(
            @RequestBody CloseAccountingYearRequest request) {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        String message = accountingYearService.yearEndClose(
                request.getAccountingYearId(),
                request.getSocietyId(),
                username);

        return ResponseEntity.ok(message);
    }

}
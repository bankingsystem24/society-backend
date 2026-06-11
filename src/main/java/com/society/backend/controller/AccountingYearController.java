package com.society.backend.controller;

import com.society.backend.entity.AccountingYear;
import com.society.backend.service.AccountingYearService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounting-year")
@CrossOrigin(origins = "*")
public class AccountingYearController {

    private final AccountingYearService accountingYearService;

    public AccountingYearController(AccountingYearService accountingYearService){
        this.accountingYearService = accountingYearService;
    };

    // =====================================================
    // GET ALL YEARS BY SOCIETY
    // =====================================================
    @GetMapping("/{societyId}")
    public List<AccountingYear> getAll(@PathVariable Long societyId) {
        return accountingYearService.getAllYears(societyId);
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
    public AccountingYear create(@RequestBody Map<String, Object> req) {

        


        Long societyId = Long.valueOf(req.get("societyId").toString());
        String fyCode = req.get("fyCode").toString();
        String username =req.get("username").toString();
        LocalDate startDate = LocalDate.parse(req.get("startDate").toString());
        LocalDate endDate = LocalDate.parse(req.get("endDate").toString());

        return accountingYearService.createYear(
                societyId,
                fyCode,
                startDate,
                endDate,
                username
        );
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
}
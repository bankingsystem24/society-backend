package com.society.backend.service;

import com.society.backend.entity.AccountingYear;
import com.society.backend.repository.AccountingYearRepository;
import com.society.backend.repository.SocietyRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.society.backend.entity.Society;
import com.society.backend.gl.dto.TrialBalanceDTO;
import com.society.backend.gl.service.TrialBalanceService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountingYearService {

    private final AccountingYearRepository accountingYearRepository;
    private final SocietyRepository societyRepository;
    private final TrialBalanceService trialBalanceService;

    @RestControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(ResponseStatusException.class)
        public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {

            Map<String, Object> response = new HashMap<>();
            response.put("status", ex.getStatusCode().value());
            response.put("message", ex.getReason());

            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(response);
        }
    }

    // =====================================================
    // CREATE FINANCIAL YEAR
    // =====================================================
    public AccountingYear createYear(Long societyId,
            String fyCode,
            LocalDate startDate,
            LocalDate endDate,
            String username) {

        Society society = societyRepository.findById(societyId)
                .orElseThrow(() -> new RuntimeException("Society not found"));

        AccountingYear year = new AccountingYear();
        year.setSociety(society);
        year.setFyCode(fyCode);
        year.setStartDate(startDate);
        year.setEndDate(endDate);
        year.setActive(false);
        year.setCreatedAt(LocalDate.now());
        year.setCreatedBy(username);
        return accountingYearRepository.save(year);
    }

    // =====================================================
    // SET ACTIVE YEAR (IMPORTANT)
    // =====================================================
    public String setActiveYear(Long societyId, Long yearId) {

        // Step 1: deactivate all
        List<AccountingYear> years = accountingYearRepository.findBySocietyId(societyId);

        for (AccountingYear y : years) {
            y.setActive(false);
        }

        accountingYearRepository.saveAll(years);

        // Step 2: activate selected
        AccountingYear selectedYear = accountingYearRepository.findById(yearId)
                .orElseThrow(() -> new RuntimeException("Year not found"));

        selectedYear.setActive(true);
        accountingYearRepository.save(selectedYear);

        return "Active financial year updated successfully";
    }

    // =====================================================
    // GET ACTIVE YEAR
    // =====================================================
    public AccountingYear getActiveYear(Long societyId) {

        return accountingYearRepository
                .findBySocietyIdAndIsActiveTrue(societyId)
                .orElse(null);
    }

    // =====================================================
    // GET ALL YEARS
    // =====================================================
    public List<AccountingYear> getAllYears() {
        return accountingYearRepository.findAll();
    }

    // =====================================================
    // GET ALL YEARS
    // =====================================================
    public List<AccountingYear> getAllYears(Long societyId) {
        return accountingYearRepository.findBySocietyId(societyId);
    }

    @Transactional
    public String closeAccountingYear(Long accountingYearId,
            Long societyId,
            String username) {

        AccountingYear accountingYear = accountingYearRepository
                .findByIdAndSociety_Id(accountingYearId, societyId)
                .orElseThrow(() -> new RuntimeException("Accounting Year not found"));

        if (accountingYear.isClosed()) {
            throw new RuntimeException("Accounting Year is already closed.");
        }

        if (!accountingYear.isActive()) {
            throw new RuntimeException("Only active Accounting Year can be closed.");
        }

        accountingYear.setClosed(true);
        accountingYear.setActive(false);
        accountingYear.setClosedDate(LocalDate.now());
        accountingYear.setClosedBy(username);

        accountingYearRepository.save(accountingYear);

        return "Accounting Year closed successfully.";
    }

    @Transactional
    public String openAccountingYear(Long accountingYearId,
            Long societyId,
            String username) {

        AccountingYear accountingYear = accountingYearRepository
                .findByIdAndSociety_Id(accountingYearId, societyId)
                .orElseThrow(() -> new RuntimeException("Accounting Year not found"));

        if (!accountingYear.isClosed()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Accounting Year is already open.");
        }

        accountingYear.setClosed(false);
        accountingYear.setActive(true);
        accountingYear.setClosedDate(null);
        accountingYear.setClosedBy(null);

        accountingYearRepository.save(accountingYear);

        return "Accounting Year closed successfully.";
    }

    public String yearEndClose(Long accountingYearId,
            Long societyId,
            String username) {

        // 1. Validate accounting year
        AccountingYear accountingYear = validateYearClosing(accountingYearId, societyId);

        // 2. Validate Trial Balance

        // 3. Generate Closing Journal

        // 4. Carry Forward Opening Balances

        // 5. Create Next Accounting Year

        // 6. Close current accounting year

        accountingYear.setClosed(true);
        accountingYear.setClosedDate(LocalDate.now());
        accountingYear.setClosedBy(username);
        accountingYear.setActive(false);

        accountingYearRepository.save(accountingYear);

        return "Year End Closing completed successfully.";
    }

    private AccountingYear validateYearClosing(Long accountingYearId,
            Long societyId) {

        AccountingYear accountingYear = accountingYearRepository
                .findByIdAndSociety_Id(accountingYearId, societyId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Accounting Year not found"));

        if (!accountingYear.isActive()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Accounting Year is not active.");
        }

        if (accountingYear.isClosed()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Accounting Year is already closed.");
        }

        validateTrialBalance(accountingYear, societyId);

        return accountingYear;
    }

    private void validateTrialBalance(AccountingYear accountingYear,
            Long societyId) {

        List<TrialBalanceDTO> trialBalance = trialBalanceService.getTrialBalance(
                societyId,
                accountingYear.getId());

        double totalDebit = 0;
        double totalCredit = 0;

        for (TrialBalanceDTO dto : trialBalance) {
            totalDebit += dto.getClosingDebit();
            totalCredit += dto.getClosingCredit();
        }

        if (Math.abs(totalDebit - totalCredit) > 0.01) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Trial Balance is not balanced. Debit = "
                            + totalDebit + ", Credit = " + totalCredit);
        }
    }


    
}

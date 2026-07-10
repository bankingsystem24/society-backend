package com.society.backend.service;

import com.society.backend.entity.AccountingYear;
import com.society.backend.repository.AccountingYearRepository;
import com.society.backend.repository.SocietyRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.society.backend.entity.Society;
import com.society.backend.gl.dto.TrialBalanceDTO;
import com.society.backend.gl.entity.GlOpeningBalance;
import com.society.backend.gl.entity.JournalEntry;
import com.society.backend.gl.enums.VoucherType;
import com.society.backend.gl.repository.GlOpeningBalanceRepository;
import com.society.backend.gl.repository.JournalEntryRepository;
import com.society.backend.gl.service.JournalService;
import com.society.backend.gl.service.ProfitLossSnapshotService;
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
    private final JournalEntryRepository journalEntryRepository;
    private final GlOpeningBalanceRepository glOpeningBalanceRepository;
    private final JournalService journalService;
    private final ProfitLossSnapshotService profitLossSnapshotService;

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

        return "Accounting Year Opened successfully.";
    }

    public String yearEndClose(Long accountingYearId, Long societyId, String username) {
        AccountingYear accountingYear = validateYearClosing(accountingYearId, societyId);
        List<TrialBalanceDTO> trialBalance = trialBalanceService.getTrialBalance(societyId,accountingYear.getId());
        
        profitLossSnapshotService.saveSnapshot(accountingYear,trialBalance,username);
        generateClosingJournal(accountingYear,trialBalance, societyId,username);
        AccountingYear nextYear = createNextAccountingYear(accountingYear, username);
        carryForwardOpeningBalances(accountingYear,nextYear,trialBalance);
        closeCurrentYear(accountingYear, username);
        accountingYearRepository.save(accountingYear);
        return "Year End Closing completed successfully.";
    }

    private AccountingYear validateYearClosing(Long accountingYearId, Long societyId) {
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

            totalDebit += safe(dto.getOpeningDebit());
            totalDebit += safe(dto.getDebit());

            totalCredit += safe(dto.getOpeningCredit());
            totalCredit += safe(dto.getCredit());
        }

        if (Math.abs(totalDebit - totalCredit) > 0.01) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Trial Balance is not balanced. Debit = "
                            + totalDebit
                            + ", Credit = "
                            + totalCredit);
        }
    }

    private double safe(Double value) {
        return value == null ? 0.0 : value;
    }

    private void generateClosingJournal(
            AccountingYear accountingYear,
            List<TrialBalanceDTO> trialBalance,
            Long societyId,
            String username) {

        // Step 3: Create Journal Header
        JournalEntry journal = new JournalEntry();

        journal.setVoucherNo("YEC-" + accountingYear.getFyCode());
        journal.setVoucherType(VoucherType.JOURNAL);
        journal.setEntryDate(accountingYear.getEndDate());
        journal.setNarration("Year End Closing " + accountingYear.getFyCode());
        journal.setReferenceType("YEAR_END");
        journal.setReferenceId(accountingYear.getId());
        journal.setStatus("POSTED");
        journal.setSocietyId(societyId);
        journal.setFinancialYearId(accountingYear.getId());
        journal.setCreatedBy(null);
        journalEntryRepository.save(journal);

        journal = journalEntryRepository.save(journal);

        int lineNo = 1;

        double totalIncome = 0;
        double totalExpense = 0;

        final Integer PROFIT_LOSS_GL = 3999;

        for (TrialBalanceDTO dto : trialBalance) {

            if ("INCOME".equalsIgnoreCase(dto.getGroupName())
                    && dto.getClosingCredit() > 0) {

                journalService.addJournalLine(
                        journal,
                        lineNo++,
                        dto.getGlCode(),
                        dto.getClosingCredit(),
                        0.0,
                        null,
                        null,
                        societyId,
                        null,
                        accountingYear.getId());

                journalService.addJournalLine(
                        journal,
                        lineNo++,
                        PROFIT_LOSS_GL,
                        0.0,
                        dto.getClosingCredit(),
                        null,
                        null,
                        societyId,
                        null,
                        accountingYear.getId());

                totalIncome += dto.getClosingCredit();
            }
        }

        for (TrialBalanceDTO dto : trialBalance) {

            if ("EXPENSES".equalsIgnoreCase(dto.getGroupName())
                    && dto.getClosingDebit() > 0) {

                journalService.addJournalLine(
                        journal,
                        lineNo++,
                        PROFIT_LOSS_GL,
                        dto.getClosingDebit(),
                        0.0,
                        null,
                        null,
                        societyId,
                        null,
                        accountingYear.getId());

                journalService.addJournalLine(
                        journal,
                        lineNo++,
                        dto.getGlCode(),
                        0.0,
                        dto.getClosingDebit(),
                        null,
                        null,
                        societyId,
                        null,
                        accountingYear.getId());
                totalExpense += dto.getClosingDebit();
            }
        }
        journal.setTotalAmount(Math.max(totalIncome, totalExpense));
        journalEntryRepository.save(journal);
    }

    private void closeCurrentYear(AccountingYear accountingYear, String username) {
        accountingYear.setClosed(true);
        accountingYear.setClosedDate(LocalDate.now());
        accountingYear.setClosedBy(username);
        accountingYear.setActive(false);
        accountingYearRepository.save(accountingYear);
    }

    private AccountingYear createNextAccountingYear(
            AccountingYear currentYear,
            String username) {

        String currentCode = currentYear.getFyCode();
        int startYear = Integer.parseInt(currentCode.substring(0, 4));
        int endYear = startYear + 1;
        String nextFyCode = endYear + "-" + String.format("%02d", (endYear + 1) % 100);
        accountingYearRepository.findBySocietyIdAndFyCode(
                currentYear.getSociety().getId(),
                nextFyCode).ifPresent(y -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Next Accounting Year already exists.");
                });
        AccountingYear nextYear = new AccountingYear();
        nextYear.setSociety(currentYear.getSociety());
        nextYear.setFyCode(nextFyCode);
        nextYear.setStartDate(currentYear.getEndDate().plusDays(1));
        nextYear.setEndDate(currentYear.getEndDate().plusYears(1));
        nextYear.setActive(true);
        nextYear.setClosed(false);
        nextYear.setCreatedBy(username);
        nextYear.setCreatedAt(LocalDate.now());
        return accountingYearRepository.save(nextYear);
    }

    private void carryForwardOpeningBalances(
            AccountingYear currentYear,
            AccountingYear nextYear,
            List<TrialBalanceDTO> trialBalance) {

        for (TrialBalanceDTO dto : trialBalance) {

            // Carry only Balance Sheet accounts
            if ("INCOME".equalsIgnoreCase(dto.getGroupName())
                    || "EXPENSES".equalsIgnoreCase(dto.getGroupName())) {
                continue;
            }

            // Skip zero balances
            if (dto.getClosingDebit() == 0.0 && dto.getClosingCredit() == 0.0) {
                continue;
            }

            GlOpeningBalance opening = new GlOpeningBalance();

            opening.setSociety(currentYear.getSociety());
            opening.setFinancialYearId(nextYear.getId());
            opening.setGlCode(dto.getGlCode());

            opening.setOpeningDebit(dto.getClosingDebit());
            opening.setOpeningCredit(dto.getClosingCredit());

            // Net balance (Debit positive, Credit negative)
            opening.setOpeningBalance(
                    dto.getClosingDebit() - dto.getClosingCredit());

            glOpeningBalanceRepository.save(opening);
        }
    }

}

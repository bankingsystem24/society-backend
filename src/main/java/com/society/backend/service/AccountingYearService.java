package com.society.backend.service;

import com.society.backend.entity.AccountingYear;
import com.society.backend.repository.AccountingYearRepository;
import com.society.backend.repository.SocietyRepository;
import com.society.backend.entity.Society;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AccountingYearService {

    @Autowired
    private AccountingYearRepository accountingYearRepository;

    @Autowired
    private SocietyRepository societyRepository;

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
    List<AccountingYear> years =
            accountingYearRepository.findBySocietyId(societyId);

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
    public List<AccountingYear> getAllYears(Long societyId) {
        return accountingYearRepository.findBySocietyId(societyId);
    }
}

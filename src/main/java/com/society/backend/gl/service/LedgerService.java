package com.society.backend.gl.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.society.backend.entity.AccountingYear;
import com.society.backend.gl.dto.LedgerDTO;
import com.society.backend.gl.repository.LedgerRepository;
import com.society.backend.repository.AccountingYearRepository;

@Service
public class LedgerService {

    @Autowired
    private LedgerRepository repository;

    @Autowired
    private AccountingYearRepository accountingYearRepository;

public List<LedgerDTO> getLedger(Long societyId, Integer glCode) {

    AccountingYear fy = accountingYearRepository
            .findBySocietyIdAndIsActiveTrue(societyId)
            .orElseThrow(() -> new RuntimeException("Active Financial Year not found"));

                System.out.println("My output:"+societyId+" "+glCode+" "+fy.getStartDate());
    Double opening = repository.getOpeningBalance(
            societyId,
            glCode,
            fy.getId()
    );

    double runningBalance = (opening != null ? opening : 0.0);

    List<LedgerDTO> txns = repository.getLedger(
            societyId,
            glCode,
            fy.getStartDate(),
            fy.getEndDate()
    );

    List<LedgerDTO> result = new ArrayList<>();

    // =========================
    // 1. OPENING ROW (FIXED)
    // =========================
    result.add(new LedgerDTO(
            fy.getStartDate().minusDays(1),
            "OPENING",
            "OPENING",
            "Opening Balance",
            runningBalance > 0 ? runningBalance : 0.0,
            runningBalance < 0 ? Math.abs(runningBalance) : 0.0,
            runningBalance,
            glCode,
            "Opening Balance",
            runningBalance >= 0 ? "DR" : "CR"
    ));

    // =========================
    // 2. TRANSACTIONS
    // =========================
    for (LedgerDTO dto : txns) {

        double debit = dto.getDebitAmount() != null ? dto.getDebitAmount() : 0.0;
        double credit = dto.getCreditAmount() != null ? dto.getCreditAmount() : 0.0;

        runningBalance = runningBalance + debit - credit;

        dto.setBalance(runningBalance);
        dto.setBalanceType(runningBalance >= 0 ? "DR" : "CR");

        result.add(dto);
    }

    return result;
}

}
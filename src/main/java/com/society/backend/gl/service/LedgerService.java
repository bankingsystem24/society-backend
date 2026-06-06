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

    Double openingBalance = repository.getOpeningBalance(
            societyId,
            glCode,
            fy.getStartDate()
    );

    double runningBalance = openingBalance != null ? openingBalance : 0.0;

    List<LedgerDTO> list = repository.getLedger(
            societyId,
            glCode,
            fy.getStartDate(),
            fy.getEndDate()
    );

    List<LedgerDTO> result = new ArrayList<>();

    // ✅ 1. ADD OPENING ROW
    result.add(new LedgerDTO(
            fy.getStartDate().minusDays(1),
            "OPENING",
            "OPENING",
            "Opening Balance",
            0.0,
            0.0,
            runningBalance,
            glCode,
            "Opening Balance",
            runningBalance >= 0 ? "DR" : "CR"
    ));

    // 2. Process transactions
    for (LedgerDTO dto : list) {

        runningBalance +=
                (dto.getDebitAmount() != null ? dto.getDebitAmount() : 0)
              - (dto.getCreditAmount() != null ? dto.getCreditAmount() : 0);

        dto.setBalance(runningBalance);
        dto.setBalanceType(runningBalance >= 0 ? "DR" : "CR");

        result.add(dto);
    }

    return result;
}

}
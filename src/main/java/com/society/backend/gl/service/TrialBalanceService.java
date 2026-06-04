package com.society.backend.gl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.society.backend.entity.AccountingYear;
import com.society.backend.repository.AccountingYearRepository;
import com.society.backend.gl.dto.TrialBalanceDTO;
import com.society.backend.gl.repository.LedgerBalanceRepository;

@Service
public class TrialBalanceService {

    @Autowired
    private LedgerBalanceRepository ledgerRepo;

    @Autowired
    private AccountingYearRepository accountingYearRepository;

public List<TrialBalanceDTO> getTrialBalance(Long societyId) {

    System.out.println("Fetching Trial Balance for Society ID: " + societyId);

    AccountingYear fy = accountingYearRepository
            .findBySocietyIdAndIsActiveTrue(societyId)
            .orElseThrow(() ->
                    new RuntimeException("Active Financial Year not found"));

    List<TrialBalanceDTO> list =
            ledgerRepo.getTrialBalance(
                    societyId,
                    fy.getStartDate(),
                    fy.getEndDate()
            );

    for (TrialBalanceDTO dto : list) {

    double debit = dto.getDebit() != null ? dto.getDebit() : 0.0;
    double credit = dto.getCredit() != null ? dto.getCredit() : 0.0;

    double balance = debit - credit;

    if (balance > 0) {
        dto.setBalanceType("DEBIT");
        dto.setBalance(balance);
    } else if (balance < 0) {
        dto.setBalanceType("CREDIT");
        dto.setBalance(Math.abs(balance));
    } else {
        dto.setBalanceType("BALANCED");
        dto.setBalance(0.0);
    }
}

    return list;
}


}
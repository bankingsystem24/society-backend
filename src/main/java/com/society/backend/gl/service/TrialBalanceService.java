package com.society.backend.gl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.society.backend.entity.AccountingYear;
import com.society.backend.repository.AccountingYearRepository;
import com.society.backend.gl.dto.TrialBalanceDTO;
import com.society.backend.gl.entity.GlOpeningBalance;
import com.society.backend.gl.repository.GlOpeningBalanceRepository;
import com.society.backend.gl.repository.LedgerBalanceRepository;

@Service
public class TrialBalanceService {

    @Autowired
    private LedgerBalanceRepository ledgerRepo;

    @Autowired
    private AccountingYearRepository accountingYearRepository;

    @Autowired
    private GlOpeningBalanceRepository glOpeningBalanceRepository;

    public List<TrialBalanceDTO> getTrialBalance(Long societyId) {

        AccountingYear fy = accountingYearRepository
                .findBySocietyIdAndIsActiveTrue(societyId)
                .orElseThrow(() -> new RuntimeException("Active Financial Year not found"));

        List<TrialBalanceDTO> list = ledgerRepo.getTrialBalance(
                societyId,
                fy.getStartDate(),
                fy.getEndDate());

        for (TrialBalanceDTO dto : list) {

            GlOpeningBalance ob = glOpeningBalanceRepository
                    .findBySocietyIdAndGlCodeAndFinancialYearId(
                            societyId,
                            dto.getGlCode(),
                            fy.getId());

            double opening = 0.0;

            if (ob != null && ob.getOpeningBalance() != null) {
                opening = ob.getOpeningBalance();
            }

            double debit = dto.getDebit() != null
                    ? dto.getDebit()
                    : 0.0;

            double credit = dto.getCredit() != null
                    ? dto.getCredit()
                    : 0.0;

            double closing = opening + debit - credit;

            dto.setOpeningBalance(Math.abs(opening));
            dto.setOpeningType(
                    opening >= 0 ? "DR" : "CR");

            dto.setClosingBalance(Math.abs(closing));
            dto.setClosingType(
                    closing >= 0 ? "DR" : "CR");

        }

        return list;
    }

}
package com.society.backend.gl.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.society.backend.entity.AccountingYear;
import com.society.backend.gl.dto.TrialBalanceDTO;
import com.society.backend.gl.repository.LedgerBalanceRepository;
import com.society.backend.repository.AccountingYearRepository;

@Service
public class TrialBalanceService {

    private final LedgerBalanceRepository ledgerRepo;
    private final AccountingYearRepository accountingYearRepository;

    public TrialBalanceService(LedgerBalanceRepository ledgerRepo,
    AccountingYearRepository accountingYearRepository
    ){
        this.ledgerRepo = ledgerRepo;
        this.accountingYearRepository = accountingYearRepository;

    }

    private double safe(Double value) {
        return value == null ? 0.0 : value;
    }

public List<TrialBalanceDTO> getTrialBalance(Long societyId, Long financialYearId) {

    // Get Active Financial Year
    AccountingYear fy = accountingYearRepository
            .findByIdAndSociety_Id(financialYearId, societyId)
            .orElseThrow(() -> new RuntimeException("Financial Year not found."));

    // Get Trial Balance
    List<TrialBalanceDTO> list = ledgerRepo.getTrialBalance(
            societyId,
            fy.getStartDate(),
            fy.getEndDate(),
            financialYearId);

    for (TrialBalanceDTO dto : list) {

        // Opening values now come from OPENING vouchers
        double openingDebit = safe(dto.getOpeningDebit());
        double openingCredit = safe(dto.getOpeningCredit());

        // Period transactions (excluding OPENING)
        double periodDebit = safe(dto.getDebit());
        double periodCredit = safe(dto.getCredit());

        // Opening
        double openingNet = openingDebit - openingCredit;

        dto.setOpeningBalance(Math.abs(openingNet));
        dto.setOpeningType(openingNet >= 0 ? "DR" : "CR");

        // Closing
        double closingNet = openingNet + (periodDebit - periodCredit);

        dto.setClosingBalance(Math.abs(closingNet));
        dto.setClosingType(closingNet >= 0 ? "DR" : "CR");

        dto.setClosingDebit(closingNet > 0 ? closingNet : 0.0);
        dto.setClosingCredit(closingNet < 0 ? Math.abs(closingNet) : 0.0);
    }

    return list;
}


}
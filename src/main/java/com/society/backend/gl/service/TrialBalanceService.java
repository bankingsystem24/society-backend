package com.society.backend.gl.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.society.backend.entity.AccountingYear;
import com.society.backend.gl.dto.TrialBalanceDTO;
import com.society.backend.gl.entity.GlOpeningBalance;
import com.society.backend.gl.repository.GlOpeningBalanceRepository;
import com.society.backend.gl.repository.LedgerBalanceRepository;
import com.society.backend.repository.AccountingYearRepository;

@Service
public class TrialBalanceService {

    private final LedgerBalanceRepository ledgerRepo;
    private final AccountingYearRepository accountingYearRepository;
    private final GlOpeningBalanceRepository glOpeningBalanceRepository;

    public TrialBalanceService(LedgerBalanceRepository ledgerRepo,
    AccountingYearRepository accountingYearRepository,
    GlOpeningBalanceRepository glOpeningBalanceRepository
    ){
        this.ledgerRepo = ledgerRepo;
        this.accountingYearRepository = accountingYearRepository;
        this.glOpeningBalanceRepository = glOpeningBalanceRepository;

    }

    private double safe(Double value) {
        return value == null ? 0.0 : value;
    }

    public List<TrialBalanceDTO> getTrialBalance(Long societyId,Long financialYearId) {

        // 1. Get Active Financial Year
        AccountingYear fy = accountingYearRepository
            .findByIdAndSociety_Id(financialYearId, societyId)
            .orElseThrow(() -> new RuntimeException(
                    "Financial Year not found."));

        // 2. Get ledger period data
        List<TrialBalanceDTO> list = ledgerRepo.getTrialBalance(
                societyId,
                fy.getStartDate(),
                fy.getEndDate(),
                financialYearId
        );

        List<GlOpeningBalance> openingList =
                glOpeningBalanceRepository.findBySociety_IdAndFinancialYearId(
                        societyId,
                        financialYearId
                );

        // 4. Convert to Map (glCode -> OpeningBalance)
        Map<Integer, GlOpeningBalance> openingMap = openingList.stream()
                .collect(Collectors.toMap(
                        GlOpeningBalance::getGlCode,
                        Function.identity(),
                        (a, b) -> a // handle duplicates safely
                ));

        // 5. Process Trial Balance
        for (TrialBalanceDTO dto : list) {

            GlOpeningBalance ob = openingMap.get(dto.getGlCode());

            double openingDebit = ob != null ? safe(ob.getOpeningDebit()) : 0.0;
            double openingCredit = ob != null ? safe(ob.getOpeningCredit()) : 0.0;

            double periodDebit = safe(dto.getDebit());
            double periodCredit = safe(dto.getCredit());

            // Opening Balance
            double openingNet = openingDebit - openingCredit;

            dto.setOpeningBalance(Math.abs(openingNet));
            dto.setOpeningType(openingNet >= 0 ? "DR" : "CR");

            // Period values
            dto.setDebit(periodDebit);
            dto.setCredit(periodCredit);

            // Closing Balance
            double periodNet = periodDebit - periodCredit;
            double closingNet = openingNet + periodNet;

            dto.setClosingBalance(Math.abs(closingNet));
            dto.setClosingType(closingNet >= 0 ? "DR" : "CR");

            dto.setClosingDebit(closingNet > 0 ? closingNet : 0);
            dto.setClosingCredit(closingNet < 0 ? Math.abs(closingNet) : 0);

        }

        return list; 
    }
}
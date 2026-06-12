package com.society.backend.gl.service;

import org.springframework.stereotype.Service;

import com.society.backend.gl.entity.LedgerBalance;
import com.society.backend.gl.repository.LedgerBalanceRepository;

@Service
public class LedgerBalanceService {

    private final LedgerBalanceRepository ledgerRepo;

    public LedgerBalanceService(LedgerBalanceRepository ledgerRepo){
        this.ledgerRepo = ledgerRepo;
    }

    public void updateBalance(Long societyId,
                              Integer glCode,
                              Long entityId,
                              String entityType,
                              double debit,
                              double credit) {

        // ================= RULES =================
        Long resolvedEntityId = entityId;
        String resolvedEntityType = entityType;

        // CASH / BANK / CONTROL ACCOUNTS
        if (glCode == 1001 || glCode == 1002) {
            resolvedEntityId = null;
            resolvedEntityType = "SOCIETY";
        }

        // MEMBER RECEIVABLE
        if (glCode == 1101) {
            resolvedEntityId = null;
            resolvedEntityType = "SOCIETY";
        }

        // INCOME ACCOUNT
        if (glCode == 4001) {
            resolvedEntityId = null;
            resolvedEntityType = "SOCIETY";
        }

        final Long finalEntityId = resolvedEntityId;
        final String finalEntityType = resolvedEntityType;

        // ================= FETCH OR CREATE =================
        LedgerBalance ledger = ledgerRepo
                .findBySocietyIdAndGlCodeAndEntityIdAndEntityType(
                        societyId,
                        glCode,
                        finalEntityId,
                        finalEntityType
                )
                .orElseGet(() -> {
                    LedgerBalance l = new LedgerBalance();
                    l.setSocietyId(societyId);
                    l.setGlCode(glCode);
                    l.setEntityId(finalEntityId);
                    l.setEntityType(finalEntityType);

                    l.setOpeningBalance(0.0);
                    l.setDebit(0.0);
                    l.setCredit(0.0);
                    l.setCurrentBalance(0.0);

                    return l;
                });

        // ================= SAFE NULL HANDLING =================
        double oldDebit = ledger.getDebit() == null ? 0 : ledger.getDebit();
        double oldCredit = ledger.getCredit() == null ? 0 : ledger.getCredit();

        // ================= UPDATE DR/CR =================
        double newDebit = oldDebit + debit;
        double newCredit = oldCredit + credit;

        ledger.setDebit(newDebit);
        ledger.setCredit(newCredit);

        // ================= BALANCE =================
        ledger.setCurrentBalance(newDebit - newCredit);

        ledgerRepo.save(ledger);
    }
}
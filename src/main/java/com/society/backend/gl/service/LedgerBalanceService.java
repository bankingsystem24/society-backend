package com.society.backend.gl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.society.backend.gl.entity.LedgerBalance;
import com.society.backend.gl.repository.LedgerBalanceRepository;

@Service
public class LedgerBalanceService {

    @Autowired
    private LedgerBalanceRepository ledgerRepo;

    public void updateBalance(Long societyId,
                            Integer glCode,
                            Long entityId,
                            String entityType,
                            double debit,
                            double credit) {

        final Long finalEntityId;
        final String finalEntityType;

        if (glCode != null && (glCode == 1001 || glCode == 1002)) {
            finalEntityId = null;
            finalEntityType = "SOCIETY";
        } else {
            finalEntityId = entityId;
            finalEntityType = entityType;
        }
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
                    l.setCurrentBalance(0.0);
                    return l;
                });

        double current = ledger.getCurrentBalance() == null ? 0 : ledger.getCurrentBalance();

        current = current + debit - credit;

        ledger.setCurrentBalance(current);

        ledgerRepo.save(ledger);
    }
}
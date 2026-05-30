package com.society.backend.gl.service;

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

    public List<LedgerDTO> getLedger(
            Long societyId,
            Integer glCode
    ) {

        AccountingYear fy = accountingYearRepository
                .findBySocietyIdAndIsActiveTrue(societyId)
                .orElseThrow(() ->
                        new RuntimeException("Active Financial Year not found"));

        List<LedgerDTO> list =
                repository.getLedger(
                        societyId,
                        glCode,
                        fy.getStartDate(),
                        fy.getEndDate()
                );

        double runningBalance = 0;

        for (LedgerDTO dto : list) {

            runningBalance +=
                    (dto.getDebitAmount() != null
                            ? dto.getDebitAmount() : 0)

                    -

                    (dto.getCreditAmount() != null
                            ? dto.getCreditAmount() : 0);

            dto.setBalance(runningBalance);
        }

        return list;
    }
}
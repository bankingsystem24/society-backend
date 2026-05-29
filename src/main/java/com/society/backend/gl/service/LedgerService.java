package com.society.backend.gl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.society.backend.gl.dto.LedgerDTO;
import com.society.backend.gl.repository.LedgerRepository;

@Service
public class LedgerService {

    @Autowired
    private LedgerRepository repository;

    public List<LedgerDTO> getLedger(
            Long societyId,
            Integer glCode
    ) {

        List<LedgerDTO> list =
                repository.getLedger(societyId, glCode);

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
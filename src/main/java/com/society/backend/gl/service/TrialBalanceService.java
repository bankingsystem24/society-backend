package com.society.backend.gl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.society.backend.gl.dto.TrialBalanceDTO;
import com.society.backend.gl.repository.LedgerBalanceRepository;

@Service
public class TrialBalanceService {

    @Autowired
    private LedgerBalanceRepository ledgerRepo;

    public List<TrialBalanceDTO> getTrialBalance(Long societyId) {

        List<TrialBalanceDTO> list = ledgerRepo.getTrialBalance(societyId);

        for (TrialBalanceDTO dto : list) {

            double balance = dto.getBalance();

            if (balance < 0) {
                dto.setBalanceType("CREDIT");
                dto.setBalance(Math.abs(balance));
            } else {
                dto.setBalanceType("DEBIT");
            }
        }

        return list;
    }
}

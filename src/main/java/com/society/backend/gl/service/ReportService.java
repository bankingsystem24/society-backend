package com.society.backend.gl.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.society.backend.gl.dto.ProfitLossItemDTO;
import com.society.backend.gl.dto.ProfitLossResponseDTO;
import com.society.backend.gl.repository.JournalEntryLineRepository;

@Service
public class ReportService {

    private final JournalEntryLineRepository journalEntryLineRepository;

    public ReportService(JournalEntryLineRepository journalEntryLineRepository){
        this.journalEntryLineRepository = journalEntryLineRepository;
    }
    
    public ProfitLossResponseDTO getProfitAndLoss(Long societyId) {

        List<Object[]> rows = journalEntryLineRepository.getProfitLossData();

        List<ProfitLossItemDTO> income = new ArrayList<>();
        List<ProfitLossItemDTO> expense = new ArrayList<>();

        double totalIncome = 0;
        double totalExpense = 0;

        for (Object[] row : rows) {

            Integer glCode =
                    ((Number) row[0]).intValue();

            String accountName =
                    String.valueOf(row[1]);

            String accountType =
                    String.valueOf(row[2]);

            Double debit =
                    ((Number) row[3]).doubleValue();

            Double credit =
                    ((Number) row[4]).doubleValue();

            if ("INCOME".equalsIgnoreCase(accountType)) {

                double amount = credit - debit;

                if (amount != 0) {

                    income.add(
                            new ProfitLossItemDTO(
                                    glCode,
                                    accountName,
                                    amount
                            )
                    );

                    totalIncome += amount;
                }
            }

            if ("EXPENSES".equalsIgnoreCase(accountType)) {

                double amount = debit - credit;

                if (amount != 0) {

                    expense.add(
                            new ProfitLossItemDTO(
                                    glCode,
                                    accountName,
                                    amount
                            )
                    );

                    totalExpense += amount;
                }
            }
        }

        ProfitLossResponseDTO response =
                new ProfitLossResponseDTO();

        response.setIncome(income);
        response.setExpense(expense);

        response.setTotalIncome(totalIncome);
        response.setTotalExpense(totalExpense);

        response.setSurplus(
                totalIncome - totalExpense
        );

        return response;
    }

}

package com.society.backend.gl.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.society.backend.gl.dto.DayBookDTO;
import com.society.backend.gl.dto.DayBookGroupDTO;
import com.society.backend.gl.dto.DayBookReportDTO;
import com.society.backend.gl.dto.OpeningBalanceDTO;
import com.society.backend.gl.repository.DayBookRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DayBookService {

    private final DayBookRepository dayBookRepository;

    public DayBookReportDTO getDayBook(Long societyId, LocalDate date) {

        List<DayBookDTO> rows = dayBookRepository.getDayBook(societyId, date);

        List<OpeningBalanceDTO> openingBalances =
                dayBookRepository.getOpeningBalance(societyId, date);

        // Add Opening Balance rows
        for (OpeningBalanceDTO ob : openingBalances) {

            if (ob.getOpeningBalance() == 0) {
                continue;
            }

            DayBookDTO opening = new DayBookDTO();
            opening.setGlCode(ob.getGlCode());
            opening.setAccountName(ob.getAccountName());
            opening.setParticulars("Opening Balance");

            if (ob.getOpeningBalance() >= 0) {
                opening.setDebitAmount(ob.getOpeningBalance());
            } else {
                opening.setCreditAmount(Math.abs(ob.getOpeningBalance()));
            }

            rows.add(0, opening);
        }

        Map<Integer, DayBookGroupDTO> debitMap = new LinkedHashMap<>();
        Map<Integer, DayBookGroupDTO> creditMap = new LinkedHashMap<>();

        double totalDebit = 0;
        double totalCredit = 0;

        for (DayBookDTO dto : rows) {

            boolean isOpening = "Opening Balance".equals(dto.getParticulars());

            if (dto.getDebitAmount() != null && dto.getDebitAmount() > 0) {

                DayBookGroupDTO group = debitMap.computeIfAbsent(
                        dto.getGlCode(),
                        k -> {
                            DayBookGroupDTO g = new DayBookGroupDTO();
                            g.setGlCode(dto.getGlCode());
                            g.setAccountName(dto.getAccountName());
                            return g;
                        });

                group.getTransactions().add(dto);
                group.setTotalDebit(group.getTotalDebit() + dto.getDebitAmount());

                if (!isOpening) {
                    totalDebit += dto.getDebitAmount();
                }
            }

            if (dto.getCreditAmount() != null && dto.getCreditAmount() > 0) {

                DayBookGroupDTO group = creditMap.computeIfAbsent(
                        dto.getGlCode(),
                        k -> {
                            DayBookGroupDTO g = new DayBookGroupDTO();
                            g.setGlCode(dto.getGlCode());
                            g.setAccountName(dto.getAccountName());
                            return g;
                        });

                group.getTransactions().add(dto);
                group.setTotalCredit(group.getTotalCredit() + dto.getCreditAmount());

                if (!isOpening) {
                    totalCredit += dto.getCreditAmount();
                }
            }
        }

        DayBookReportDTO report = new DayBookReportDTO();
        report.setDebitGroups(new ArrayList<>(debitMap.values()));
        report.setCreditGroups(new ArrayList<>(creditMap.values()));
        report.setTotalDebit(totalDebit);
        report.setTotalCredit(totalCredit);

        return report;
    }


}
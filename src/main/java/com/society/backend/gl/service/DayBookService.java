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
import com.society.backend.gl.repository.DayBookRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DayBookService {

    private final DayBookRepository dayBookRepository;

public DayBookReportDTO getDayBook(Long societyId, LocalDate date) {

    // Transactions for selected date
    List<DayBookDTO> rows = dayBookRepository.getDayBook(societyId, date);

    // Opening balance before selected date
    Double openingBalance = dayBookRepository.getOpeningBalance(societyId, date);

    if (openingBalance == null) {
        openingBalance = 0.0;
    }

    // Add Opening Balance row
    if (openingBalance != 0) {

        DayBookDTO opening = new DayBookDTO();
        opening.setGlCode(1001);               // Cash in Hand
        opening.setAccountName("Cash in Hand");
        opening.setParticulars("Opening Balance");

        if (openingBalance >= 0) {
            opening.setDebitAmount(openingBalance);
        } else {
            opening.setCreditAmount(Math.abs(openingBalance));
        }

        rows.add(0, opening);
    }

    Map<Integer, DayBookGroupDTO> debitMap = new LinkedHashMap<>();
    Map<Integer, DayBookGroupDTO> creditMap = new LinkedHashMap<>();

    double totalDebit = 0;
    double totalCredit = 0;

for (DayBookDTO dto : rows) {

    boolean isOpening = "Opening Balance".equals(dto.getParticulars());

    // ---------------- DEBIT ----------------
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

        // Don't include opening balance in daily total
        if (!isOpening) {
            totalDebit += dto.getDebitAmount();
        }
    }

    // ---------------- CREDIT ----------------
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

        // Don't include opening balance in daily total
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
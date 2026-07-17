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

        List<DayBookDTO> rows = dayBookRepository.getDayBook(societyId, date);

        Map<Integer, DayBookGroupDTO> debitMap = new LinkedHashMap<>();
        Map<Integer, DayBookGroupDTO> creditMap = new LinkedHashMap<>();

        double totalDebit = 0.0;
        double totalCredit = 0.0;

        for (DayBookDTO dto : rows) {

            // ---------------- DEBIT SIDE ----------------

            if (dto.getDebitAmount() != null && dto.getDebitAmount() > 0) {

                DayBookGroupDTO group = debitMap.get(dto.getGlCode());

                if (group == null) {

                    group = new DayBookGroupDTO();
                    group.setGlCode(dto.getGlCode());
                    group.setAccountName(dto.getAccountName());

                    debitMap.put(dto.getGlCode(), group);
                }

                group.getTransactions().add(dto);

                group.setTotalDebit(
                        group.getTotalDebit() + dto.getDebitAmount());

                totalDebit += dto.getDebitAmount();
            }

            // ---------------- CREDIT SIDE ----------------

            if (dto.getCreditAmount() != null && dto.getCreditAmount() > 0) {

                DayBookGroupDTO group = creditMap.get(dto.getGlCode());

                if (group == null) {

                    group = new DayBookGroupDTO();
                    group.setGlCode(dto.getGlCode());
                    group.setAccountName(dto.getAccountName());

                    creditMap.put(dto.getGlCode(), group);
                }

                group.getTransactions().add(dto);

                group.setTotalCredit(
                        group.getTotalCredit() + dto.getCreditAmount());

                totalCredit += dto.getCreditAmount();
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
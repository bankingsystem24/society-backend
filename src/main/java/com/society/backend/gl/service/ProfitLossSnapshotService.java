package com.society.backend.gl.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.society.backend.entity.AccountingYear;
import com.society.backend.gl.dto.ProfitLossSnapshotDetailRequest;
import com.society.backend.gl.dto.ProfitLossSnapshotDetailResponse;
import com.society.backend.gl.dto.ProfitLossSnapshotRequest;
import com.society.backend.gl.dto.ProfitLossSnapshotResponse;
import com.society.backend.gl.dto.TrialBalanceDTO;
import com.society.backend.gl.entity.ProfitLossSnapshotDetail;
import com.society.backend.gl.entity.ProfitLossSnapshotHeader;
import com.society.backend.gl.repository.ProfitLossSnapshotDetailRepository;
import com.society.backend.gl.repository.ProfitLossSnapshotHeaderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfitLossSnapshotService {

    private final ProfitLossSnapshotHeaderRepository headerRepository;
    private final ProfitLossSnapshotDetailRepository detailRepository;

    @Transactional
    public void newSaveSnapshot(ProfitLossSnapshotRequest request) {

        // Delete existing snapshot for the financial year
        headerRepository.findByFinancialYearId(request.getFinancialYearId())
                .ifPresent(existing -> {
                    detailRepository.deleteByHeaderId(existing.getId());
                    headerRepository.delete(existing);
                });

        ProfitLossSnapshotHeader header = new ProfitLossSnapshotHeader();
        header.setSocietyId(request.getSocietyId());
        header.setFinancialYearId(request.getFinancialYearId());
        header.setRemarks(request.getRemarks());
        header.setCreatedBy(request.getCreatedBy());
        header.setCreatedAt(LocalDateTime.now());

        double totalIncome = 0.0;
        double totalExpense = 0.0;

        header = headerRepository.save(header);

        if (request.getDetails() != null) {

            for (ProfitLossSnapshotDetailRequest dto : request.getDetails()) {

                ProfitLossSnapshotDetail detail = new ProfitLossSnapshotDetail();

                detail.setHeader(header);
                detail.setGlCode(dto.getGlCode());
                detail.setAccountName(dto.getAccountName());
                detail.setAccountType(dto.getAccountType());
                detail.setAmount(dto.getAmount());

                detailRepository.save(detail);

                if ("INCOME".equalsIgnoreCase(dto.getAccountType())) {
                    totalIncome += dto.getAmount();
                } else if ("EXPENSE".equalsIgnoreCase(dto.getAccountType())) {
                    totalExpense += dto.getAmount();
                }
            }
        }

        header.setTotalIncome(totalIncome);
        header.setTotalExpense(totalExpense);
        header.setNetProfitLoss(totalIncome - totalExpense);

        headerRepository.save(header);
    }

    @Transactional
    public void saveSnapshot(
            AccountingYear accountingYear,
            List<TrialBalanceDTO> trialBalance,
            String username) {

        // Delete existing snapshot for the financial year
        headerRepository.findByFinancialYearId(accountingYear.getId().longValue())
                .ifPresent(existing -> {
                    detailRepository.deleteByHeaderId(existing.getId());
                    headerRepository.delete(existing);
                });

        ProfitLossSnapshotHeader header = new ProfitLossSnapshotHeader();
        header.setSocietyId(accountingYear.getSociety().getId());
        header.setFinancialYearId(accountingYear.getId().longValue());
        header.setRemarks("Year End Closing Snapshot");
        header.setCreatedBy(Long.parseLong(username));
        header.setCreatedAt(LocalDateTime.now());

        double totalIncome = 0.0;
        double totalExpense = 0.0;

        header = headerRepository.save(header);

        for (TrialBalanceDTO tb : trialBalance) {

            if (tb.getGroupName() == null) {
                continue;
            }

            String group = tb.getGroupName().trim().toUpperCase();

            // Save only Income & Expense accounts
            if (!group.equals("INCOME") && !group.equals("EXPENSE")) {
                continue;
            }

            ProfitLossSnapshotDetail detail = new ProfitLossSnapshotDetail();

            detail.setHeader(header);
            detail.setGlCode(tb.getGlCode());
            detail.setAccountName(tb.getAccountName());
            detail.setAccountType(group);

            double amount;

            if (group.equals("INCOME")) {
                amount = tb.getCredit() == null ? 0.0 : tb.getCredit();
                totalIncome += amount;
            } else {
                amount = tb.getDebit() == null ? 0.0 : tb.getDebit();
                totalExpense += amount;
            }

            detail.setAmount(amount);

            detailRepository.save(detail);
        }

        header.setTotalIncome(totalIncome);
        header.setTotalExpense(totalExpense);
        header.setNetProfitLoss(totalIncome - totalExpense);

        headerRepository.save(header);
    }

    @Transactional
    public ProfitLossSnapshotResponse getSnapshot(Long financialYearId) {

        ProfitLossSnapshotHeader header = headerRepository
                .findByFinancialYearId(financialYearId)
                .orElseThrow(() -> new RuntimeException("Profit Loss Snapshot not found."));

        ProfitLossSnapshotResponse response = mapHeader(header);

        List<ProfitLossSnapshotDetail> details = detailRepository.findByHeaderId(header.getId());

        List<ProfitLossSnapshotDetailResponse> responseDetails = new ArrayList<>();

        for (ProfitLossSnapshotDetail detail : details) {
            responseDetails.add(mapDetail(detail));
        }

        response.setDetails(responseDetails);

        return response;
    }

    @Transactional
    public List<ProfitLossSnapshotResponse> getAllSnapshots() {

        List<ProfitLossSnapshotResponse> responseList = new ArrayList<>();

        for (ProfitLossSnapshotHeader header : headerRepository.findAll()) {

            ProfitLossSnapshotResponse dto = mapHeader(header);

            List<ProfitLossSnapshotDetailResponse> details = new ArrayList<>();

            for (ProfitLossSnapshotDetail detail : detailRepository.findByHeaderId(header.getId())) {

                details.add(mapDetail(detail));
            }

            dto.setDetails(details);

            responseList.add(dto);
        }

        return responseList;
    }

    @Transactional
    public void deleteSnapshot(Long financialYearId) {

        ProfitLossSnapshotHeader header = headerRepository
                .findByFinancialYearId(financialYearId)
                .orElseThrow(() -> new RuntimeException("Profit Loss Snapshot not found."));

        detailRepository.deleteByHeaderId(header.getId());
        headerRepository.delete(header);
    }

    private ProfitLossSnapshotResponse mapHeader(
            ProfitLossSnapshotHeader header) {

        ProfitLossSnapshotResponse dto = new ProfitLossSnapshotResponse();

        dto.setId(header.getId());
        dto.setSocietyId(header.getSocietyId());
        dto.setFinancialYearId(header.getFinancialYearId());
        dto.setRemarks(header.getRemarks());
        dto.setCreatedBy(header.getCreatedBy());
        dto.setCreatedAt(header.getCreatedAt());
        dto.setTotalIncome(header.getTotalIncome());
        dto.setTotalExpense(header.getTotalExpense());
        dto.setNetProfitLoss(header.getNetProfitLoss());

        return dto;
    }

    private ProfitLossSnapshotDetailResponse mapDetail(
            ProfitLossSnapshotDetail detail) {

        ProfitLossSnapshotDetailResponse dto = new ProfitLossSnapshotDetailResponse();

        dto.setId(detail.getId());
        dto.setGlCode(detail.getGlCode());
        dto.setAccountName(detail.getAccountName());
        dto.setAccountType(detail.getAccountType());
        dto.setAmount(detail.getAmount());

        return dto;
    }
}
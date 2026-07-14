package com.society.backend.gl.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.society.backend.gl.dto.BalanceSheetDTO;
import com.society.backend.gl.dto.BalanceSheetDetailDTO;
import com.society.backend.gl.dto.SaveBalanceSheetRequest;
import com.society.backend.gl.entity.BalanceSheetDetail;
import com.society.backend.gl.entity.BalanceSheetHeader;
import com.society.backend.gl.repository.BalanceSheetDetailRepository;
import com.society.backend.gl.repository.BalanceSheetHeaderRepository;

@Service
public class BalanceSheetSnapshotService {

    private final BalanceSheetHeaderRepository headerRepository;
    private final BalanceSheetDetailRepository detailRepository;

    public BalanceSheetSnapshotService(
            BalanceSheetHeaderRepository headerRepository,
            BalanceSheetDetailRepository detailRepository) {

        this.headerRepository = headerRepository;
        this.detailRepository = detailRepository;
    }

    @Transactional
    public void saveSnapshot(SaveBalanceSheetRequest request) {

        // Delete existing snapshot if available
        headerRepository.findBySocietyIdAndFinancialYearId(
                request.getSocietyId(),
                request.getFinancialYearId())
                .ifPresent(header -> {
                    detailRepository.deleteByHeader(header);
                    headerRepository.delete(header);
                });

        BalanceSheetHeader header = new BalanceSheetHeader();
        header.setSocietyId(request.getSocietyId());
        header.setFinancialYearId(request.getFinancialYearId());
        header.setFinancialYear(request.getFinancialYear());
        header.setCreatedBy(request.getCreatedBy());
        header.setCreatedAt(LocalDateTime.now());

        header = headerRepository.save(header);

        double totalAssets = 0.0;
        double totalLiabilities = 0.0;
        double totalEquity = 0.0;

        for (BalanceSheetDetailDTO dto : request.getDetails()) {

            BalanceSheetDetail detail = new BalanceSheetDetail();
            detail.setHeader(header);
            detail.setGlCode(dto.getGlCode());
            detail.setAccountName(dto.getAccountName());
            detail.setAccountType(dto.getAccountType());
            detail.setAmount(dto.getAmount());

            detailRepository.save(detail);

            if ("ASSET".equalsIgnoreCase(dto.getAccountType())) {
                totalAssets += dto.getAmount();
            } else if ("LIABILITY".equalsIgnoreCase(dto.getAccountType())) {
                totalLiabilities += dto.getAmount();
            } else if ("EQUITY".equalsIgnoreCase(dto.getAccountType())) {
                totalEquity += dto.getAmount();
            }
        }

        header.setTotalAssets(totalAssets);
        header.setTotalLiabilities(totalLiabilities);
        header.setTotalEquity(totalEquity);

        headerRepository.save(header);
    }

    @Transactional(readOnly = true)
    public BalanceSheetDTO getBalanceSheet(Long financialYearId) {

        BalanceSheetHeader header = headerRepository
                .findByFinancialYearId(financialYearId)
                .orElseThrow(() ->
                        new RuntimeException("Balance Sheet Snapshot not found"));

        List<BalanceSheetDetail> details =
                detailRepository.findByHeader(header);

        BalanceSheetDTO dto = new BalanceSheetDTO();

        dto.setId(header.getId());
        dto.setSocietyId(header.getSocietyId());
        dto.setFinancialYearId(header.getFinancialYearId());
        dto.setFinancialYear(header.getFinancialYear());
        dto.setTotalAssets(header.getTotalAssets());
        dto.setTotalLiabilities(header.getTotalLiabilities());
        dto.setTotalEquity(header.getTotalEquity());
        dto.setCreatedBy(header.getCreatedBy());

        if (header.getCreatedAt() != null) {
            dto.setCreatedAt(header.getCreatedAt().toString());
        }

        List<BalanceSheetDetailDTO> detailDTOs = new ArrayList<>();

        for (BalanceSheetDetail detail : details) {

            BalanceSheetDetailDTO d = new BalanceSheetDetailDTO();
            d.setId(detail.getId());
            d.setGlCode(detail.getGlCode());
            d.setAccountName(detail.getAccountName());
            d.setAccountType(detail.getAccountType());
            d.setAmount(detail.getAmount());

            detailDTOs.add(d);
        }

        dto.setDetails(detailDTOs);

        return dto;
    }
}
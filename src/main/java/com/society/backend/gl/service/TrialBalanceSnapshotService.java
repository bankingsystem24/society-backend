package com.society.backend.gl.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.society.backend.gl.dto.SaveTrialBalanceRequest;
import com.society.backend.gl.dto.TrialBalanceDetailDTO;
import com.society.backend.gl.entity.TrialBalanceDetail;
import com.society.backend.gl.entity.TrialBalanceHeader;
import com.society.backend.gl.repository.TrialBalanceDetailRepository;
import com.society.backend.gl.repository.TrialBalanceHeaderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrialBalanceSnapshotService {

    private final TrialBalanceHeaderRepository headerRepository;
    private final TrialBalanceDetailRepository detailRepository;


    @Transactional
    public void saveSnapshot(SaveTrialBalanceRequest request) {


        if(request.getDetails() == null || request.getDetails().isEmpty()){
            throw new RuntimeException(
                "Trial Balance details cannot be empty"
            );
        }


        double totalDebit = 0.0;
        double totalCredit = 0.0;


        for (TrialBalanceDetailDTO dto : request.getDetails()) {

            totalDebit += dto.getDebitAmount() == null 
                    ? 0 
                    : dto.getDebitAmount();

            totalCredit += dto.getCreditAmount() == null 
                    ? 0 
                    : dto.getCreditAmount();
        }


        double difference =
                Math.round((totalDebit - totalCredit) * 100.0) / 100.0;


        if(difference != 0){

            throw new RuntimeException(
                "Trial Balance is not balanced. Debit="
                + totalDebit
                + ", Credit="
                + totalCredit
            );
        }



        // Remove old snapshot
        headerRepository.findByFinancialYearId(request.getFinancialYearId())
                .ifPresent(header -> {

                    detailRepository.deleteAll(
                        detailRepository.findByHeader_Id(header.getId())
                    );

                    headerRepository.delete(header);
                });



        TrialBalanceHeader header = new TrialBalanceHeader();

        header.setSocietyId(request.getSocietyId());
        header.setFinancialYearId(request.getFinancialYearId());
        header.setRemarks(request.getRemarks());
        header.setCreatedBy(request.getCreatedBy());
        header.setCreatedAt(LocalDateTime.now());

        header.setTotalDebit(totalDebit);
        header.setTotalCredit(totalCredit);


        TrialBalanceHeader savedHeader =
                headerRepository.save(header);



        for(TrialBalanceDetailDTO dto : request.getDetails()){


            TrialBalanceDetail detail =
                    new TrialBalanceDetail();


            detail.setHeader(savedHeader);

            detail.setGlCode(dto.getGlCode());

            detail.setAccountName(dto.getAccountName());

            detail.setDebitAmount(
                dto.getDebitAmount()==null 
                ? 0 
                : dto.getDebitAmount()
            );

            detail.setCreditAmount(
                dto.getCreditAmount()==null 
                ? 0 
                : dto.getCreditAmount()
            );

            detail.setAccountType(dto.getAccountType());


            detailRepository.save(detail);
        }
    }


    @Transactional(readOnly = true)
    public List<TrialBalanceDetailDTO> getSnapshot(Long financialYearId) {

    TrialBalanceHeader header = headerRepository
            .findByFinancialYearId(financialYearId)
            .orElseThrow(() ->
                    new RuntimeException("Snapshot not found"));

    List<TrialBalanceDetail> details =
            detailRepository.findByHeader_Id(header.getId());

        return details.stream().map(detail -> {

            TrialBalanceDetailDTO dto = new TrialBalanceDetailDTO();

            dto.setGlCode(detail.getGlCode());
            dto.setAccountName(detail.getAccountName());
            dto.setDebitAmount(detail.getDebitAmount());
            dto.setCreditAmount(detail.getCreditAmount());
            dto.setAccountType(detail.getAccountType());

            return dto;

        }).toList();
    }


}
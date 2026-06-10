package com.society.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.society.backend.dto.SinkingFundResponse;
import com.society.backend.entity.Flat;
import com.society.backend.entity.SinkingFund;
import com.society.backend.entity.Society;
import com.society.backend.repository.FlatRepository;
import com.society.backend.repository.SinkingFundRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Service
public class SinkingFundService {

    @Autowired
    private SinkingFundRepository repository;

    @Autowired
    private FlatRepository flatRepository;

    // ================= GENERATE =================
    public void generate(Long societyId, String month, int year, Double amount, Long createdBy) {

        List<Flat> flats = flatRepository.findBySociety_Id(societyId);

        Month billingMonth;
        try {
            billingMonth = Month.valueOf(month.toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Invalid month: " + month);
        }

        List<SinkingFund> existingList =
                repository.findBySocietyIdAndMonthAndYear(societyId, month, year);

        for (Flat flat : flats) {

            boolean exists = existingList.stream()
                    .anyMatch(x -> x.getFlat().getId().equals(flat.getId()));

            if (exists) continue;

            SinkingFund sf = new SinkingFund();

            Society society = new Society();
            society.setId(societyId);

            sf.setSociety(society);
            sf.setFlat(flat);
            sf.setMonth(month);
            sf.setYear(year);
            sf.setAmount(amount);
            sf.setCreatedBy(createdBy);
            sf.setCreatedDate(LocalDate.of(year, billingMonth.getValue(), 1));

            repository.save(sf);
        }
    }

    // ================= GET ALL (DTO) =================
    public List<SinkingFundResponse> getAll(Long societyId) {

        List<SinkingFund> list = repository.findBySociety_Id(societyId);

        return list.stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ================= GET BY FLAT =================
    public List<SinkingFund> getByFlat(Long societyId, Long flatId) {
        return repository.findBySocietyIdAndFlat_Id(societyId, flatId);
    }

    // ================= MAPPER =================
    private SinkingFundResponse mapToResponse(SinkingFund sf) {

        SinkingFundResponse dto = new SinkingFundResponse();

        dto.setId(sf.getId());
        dto.setSocietyId(sf.getSociety().getId());
        dto.setFlatId(sf.getFlat().getId());
        dto.setFlatNo(sf.getFlat().getFlatNo());

        dto.setMonth(sf.getMonth());
        dto.setYear(sf.getYear());
        dto.setAmount(sf.getAmount());
        dto.setCreatedBy(sf.getCreatedBy());
        dto.setCreatedDate(sf.getCreatedDate());
        if (sf.getFlat() != null && sf.getFlat().getOwner() != null) {
            dto.setMemberName(sf.getFlat().getOwner().getName());
        } else {
            dto.setMemberName(null);
        }
        dto.setStatus(sf.getStatus() != null ? sf.getStatus().name() : "PENDING");

        return dto;
    }
}
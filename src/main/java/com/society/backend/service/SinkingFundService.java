package com.society.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.society.backend.dto.SinkingFundResponse;
import com.society.backend.entity.Flat;
import com.society.backend.entity.Member;
import com.society.backend.entity.Receipt;
import com.society.backend.entity.SinkingFund;
import com.society.backend.entity.Society;
import com.society.backend.enums.PaymentStatus;
import com.society.backend.gl.service.JournalService;
import com.society.backend.repository.FlatRepository;
import com.society.backend.repository.ReceiptRepository;
import com.society.backend.repository.SinkingFundRepository;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Service
public class SinkingFundService {

    @Autowired
    private SinkingFundRepository sinkingFundRepository;

    @Autowired
    private FlatRepository flatRepository;

    @Autowired
    private JournalService journalService;

    @Autowired
    private ReceiptRepository receiptRepository;

    // ================= GENERATE =================
@Transactional
public void generate(Long societyId,
                     String month,
                     int year,
                     Double amount,
                     Long createdBy) {

    List<Flat> flats = flatRepository.findBySociety_Id(societyId);

    Month billingMonth;
    try {
        billingMonth = Month.valueOf(month.toUpperCase());
    } catch (Exception e) {
        throw new RuntimeException("Invalid month: " + month);
    }

    List<SinkingFund> existingList =
            sinkingFundRepository.findBySocietyIdAndMonthAndYear(
                    societyId,
                    month,
                    year);

    for (Flat flat : flats) {

        boolean exists = existingList.stream()
                .anyMatch(x -> x.getFlat().getId().equals(flat.getId()));

        if (exists) {
            continue;
        }

        SinkingFund sf = new SinkingFund();

        Society society = new Society();
        society.setId(societyId);

        sf.setSociety(society);
        sf.setFlat(flat);
        sf.setMonth(month);
        sf.setYear(year);
        sf.setAmount(amount);
        sf.setCreatedBy(createdBy);
        sf.setCreatedDate(
                LocalDate.of(year, billingMonth.getValue(), 1));

        SinkingFund savedFund = sinkingFundRepository.save(sf);

        Member member = flat.getOwner();

        try {

            Long journalId =
                    journalService.createSinkingFundEntry(
                            savedFund.getId(),
                            member,
                            amount,
                            societyId,
                            createdBy,
                            flat.getId());

            if (journalId == null) {
                throw new RuntimeException(
                        "Journal not created for sinking fund "
                                + savedFund.getId());
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Journal failed for sinkingFundId="
                            + savedFund.getId()
                            + " -> "
                            + e.getMessage());
        }
    }
}
    // ================= GET ALL (DTO) =================
    public List<SinkingFundResponse> getAll(Long societyId) {

        List<SinkingFund> list = sinkingFundRepository.findBySociety_Id(societyId);

        return list.stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ================= GET BY FLAT =================
    public List<SinkingFund> getByFlat(Long societyId, Long flatId) {
        return sinkingFundRepository.findBySocietyIdAndFlat_Id(societyId, flatId);
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

@Transactional
public String pay(List<Long> sinkingFundIds, String paymentMode) {

    List<SinkingFund> funds =
            sinkingFundRepository.findAllById(sinkingFundIds);

    if (funds.isEmpty()) {
        return "No records found";
    }

    SinkingFund first = funds.get(0);

    Long societyId = first.getSociety().getId();
    Long flatId = first.getFlat().getId();

    Double totalAmount = 0.0;

    for (SinkingFund fund : funds) {

        if (!PaymentStatus.PENDING.equals(fund.getStatus())) {
            continue;
        }

        fund.setStatus(PaymentStatus.PAID);
        fund.setPaymentMode(paymentMode);
        fund.setPaidDate(LocalDate.now());

        totalAmount += fund.getAmount();
    }

    sinkingFundRepository.saveAll(funds);

    // ================= RECEIPT =================

    Receipt receipt = new Receipt();

    receipt.setReceiptDate(LocalDate.now());
    receipt.setPaymentMode(paymentMode);

    receipt.setMaintenanceAmount(0.0);
    receipt.setInterestAmount(0.0);
    receipt.setDiscountAmount(0.0);

    // Add separate sinking fund field if available
    receipt.setTotalAmount(totalAmount);

    receipt.setSocietyId(societyId);
    receipt.setFlatId(flatId);

    Receipt savedReceipt = receiptRepository.save(receipt);

    savedReceipt.setReceiptNo(
            "SF-" +
            LocalDate.now().getYear() +
            "-" +
            savedReceipt.getId());

    savedReceipt = receiptRepository.save(savedReceipt);

    for (SinkingFund fund : funds) {
        fund.setReceiptId(savedReceipt.getId());
    }

    sinkingFundRepository.saveAll(funds);

    // ================= MEMBER =================

    Long memberId = null;

    if (first.getFlat() != null &&
        first.getFlat().getOwner() != null) {

        memberId = first.getFlat().getOwner().getId();
    }

    // ================= JOURNAL =================

    if (totalAmount > 0) {

        journalService.createReceiptEntry(
                savedReceipt.getId(),
                memberId,
                totalAmount,
                0.0,
                0.0,
                totalAmount,
                paymentMode,
                societyId,
                0L,
                flatId);
    }

    return "Sinking Fund paid successfully";
}

}
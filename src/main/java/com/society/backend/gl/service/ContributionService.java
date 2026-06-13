package com.society.backend.gl.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.society.backend.dto.CompulsoryContributionRequest;
import com.society.backend.entity.Flat;
import com.society.backend.entity.Member;
import com.society.backend.entity.Receipt;
import com.society.backend.enums.PaymentStatus;
import com.society.backend.gl.dto.ContributionResponse;
import com.society.backend.gl.entity.Contribution;
import com.society.backend.gl.repository.ContributionRepository;
import com.society.backend.repository.FlatRepository;
import com.society.backend.repository.ReceiptRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContributionService {

    private final ContributionRepository contributionRepository;
    private final FlatRepository flatRepository;
    private final JournalService journalService;
    private final ReceiptRepository receiptRepository;

    public List<ContributionResponse> getCompulsoryContributions(Long societyId, Long financialYearId) {

        List<Contribution> list = contributionRepository.findBySocietyIdAndTypeAndFinancialYearId(societyId,
                "COMPULSORY", financialYearId);

        return list.stream().map(c -> {

            ContributionResponse dto = new ContributionResponse();

            dto.setId(c.getId());
            dto.setName(c.getName());
            dto.setAmount(c.getAmount());
            dto.setMode(c.getMode());
            dto.setDate(c.getDate());
            dto.setDueDate(c.getDueDate());
            dto.setStatus(c.getStatus().name());
            dto.setSocietyId(c.getSocietyId());
            dto.setMemberId(c.getMemberId());
            dto.setFinancialYearId(financialYearId);

            if (c.getFlat() != null) {
                dto.setFlatNo(c.getFlat().getFlatNo());
                dto.setAreaSqFt(c.getFlat().getAreaSqFt());
            }

            return dto;

        }).toList();
    }

    public void createCompulsoryContribution(Long societyId, Long financialYearId, CompulsoryContributionRequest req) {

        List<Flat> flats = flatRepository.findBySociety_Id(societyId);

        List<Contribution> contributions = new ArrayList<>();

        for (Flat f : flats) {

            double amount;

            if ("FLAT".equalsIgnoreCase(req.getMode())) {
                amount = req.getFlatAmount();
            } else {
                amount = f.getAreaSqFt() * req.getRate();
            }

            Contribution c = new Contribution();
            c.setSocietyId(societyId);
            c.setMemberId(f.getId());
            c.setName(req.getName());
            c.setType("COMPULSORY");
            c.setMode(req.getMode());
            c.setFlat(f);
            c.setAmount(amount);
            c.setDueDate(req.getDueDate());
            c.setDate(req.getDate());
            c.setDescription(req.getDescription());
            c.setCreatedBy(req.getUserId());
            c.setFinancialYearId(financialYearId);

            contributions.add(c);
        }

        // ✅ SAVE ONLY ONCE
        List<Contribution> savedContributions = contributionRepository.saveAll(contributions);

        // ✅ JOURNAL POSTING
        for (Contribution c : savedContributions) {

            Member member = c.getFlat().getOwner();

            try {

                Long journalId = journalService.createContributionEntry(
                        c.getId(),
                        member,
                        c.getAmount(),
                        societyId,
                        c.getCreatedBy(),
                        c.getFlat().getId(),
                        c.getFinancialYearId());

                if (journalId == null) {
                    throw new RuntimeException(
                            "Journal not created for contribution " + c.getId());
                }

            } catch (Exception e) {

                throw new RuntimeException(
                        "Journal failed for contributionId="
                                + c.getId()
                                + " -> " + e.getMessage());
            }
        }
    }

    @Transactional
    public String pay(List<Long> contributionIds,
            String paymentMode,
            Long financialYearId) {

        List<Contribution> contributions = contributionRepository.findAllById(contributionIds);

        if (contributions.isEmpty()) {
            return "No records found";
        }

        Contribution first = contributions.get(0);

        Long societyId = first.getSocietyId();
        Long flatId = first.getFlat().getId();

        Double totalAmount = 0.0;

        for (Contribution contribution : contributions) {

            if (!PaymentStatus.PENDING.equals(contribution.getStatus())) {
                continue;
            }

            contribution.setStatus(PaymentStatus.PAID);
            contribution.setPaymentMode(paymentMode);
            contribution.setFinancialYearId(financialYearId);

            totalAmount += contribution.getAmount();
        }

        contributionRepository.saveAll(contributions);

        // ================= RECEIPT =================

        Receipt receipt = new Receipt();

        receipt.setReceiptDate(LocalDate.now());
        receipt.setPaymentMode(paymentMode);

        receipt.setMaintenanceAmount(0.0);
        receipt.setInterestAmount(0.0);
        receipt.setDiscountAmount(0.0);

        receipt.setTotalAmount(totalAmount);

        receipt.setSocietyId(societyId);
        receipt.setFlatId(flatId);
        receipt.setFinancialYearId(financialYearId);

        Receipt savedReceipt = receiptRepository.save(receipt);

        savedReceipt.setReceiptNo(
                "CON-" +
                        LocalDate.now().getYear() +
                        "-" +
                        savedReceipt.getId());

        savedReceipt = receiptRepository.save(savedReceipt);

        // ================= UPDATE CONTRIBUTIONS =================

        for (Contribution contribution : contributions) {
            contribution.setReceiptId(savedReceipt.getId());
        }

        contributionRepository.saveAll(contributions);

        // ================= MEMBER =================

        Long memberId = null;

        if (first.getFlat() != null &&
                first.getFlat().getOwner() != null) {

            memberId = first.getFlat().getOwner().getId();
        }

        // ================= JOURNAL ENTRY =================

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
                    flatId,
                    financialYearId);
        }

        return "Contribution paid successfully";
    }
}

package com.society.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.society.backend.dto.ReceiptDetailsResponse;
import com.society.backend.dto.ReceiptRequest;
import com.society.backend.dto.ReceiptResponse;
import com.society.backend.entity.Billing;
import com.society.backend.entity.Flat;
import com.society.backend.entity.Receipt;
import com.society.backend.entity.SinkingFund;
import com.society.backend.gl.entity.Contribution;
import com.society.backend.gl.repository.ContributionRepository;
import com.society.backend.repository.BillingRepository;
import com.society.backend.repository.FlatRepository;
import com.society.backend.repository.ReceiptRepository;
import com.society.backend.repository.SinkingFundRepository;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

@Service
public class ReceiptService {

        private final ReceiptRepository receiptRepository;
        private final BillingRepository billingRepository;
        private final FlatRepository flatRepository;
        private final ContributionRepository contributionRepository;
        private final SinkingFundRepository sinkingFundRepository;

        public ReceiptService(ReceiptRepository receiptRepository,
                        BillingRepository billingRepository,
                        FlatRepository flatRepository,
                        ContributionRepository contributionRepository,
                        SinkingFundRepository sinkingFundRepository) {
                this.receiptRepository = receiptRepository;
                this.billingRepository = billingRepository;
                this.flatRepository = flatRepository;
                this.contributionRepository = contributionRepository;
                this.sinkingFundRepository = sinkingFundRepository;
        }

        @Transactional
        public Receipt createReceipt(ReceiptRequest req) {

                // 1. SAVE RECEIPT
                Receipt receipt = new Receipt();
                receipt.setReceiptNo(req.getReceiptNo());
                receipt.setSocietyId(req.getSocietyId());
                receipt.setFlatId(req.getFlatId());
                receipt.setTotalAmount(req.getTotalAmount());
                receipt.setPaymentMode(req.getPaymentMode());
                receipt.setMaintenanceAmount(req.getMaintenanceAmount());
                receipt.setInterestAmount(req.getInterestAmount());
                receipt.setDiscountAmount(req.getDiscountAmount());

                Receipt savedReceipt = receiptRepository.save(receipt);

                return savedReceipt;
        }

        public List<ReceiptResponse> viewReceipts(
                        Long societyId,
                        Long flatId,
                        Long financialYearId) {

                List<Receipt> receipts = receiptRepository.findBySocietyIdAndFinancialYearId(
                                societyId,
                                financialYearId);

                if (societyId == null) {
                        receipts = receiptRepository.findByFinancialYearId(
                                financialYearId);
                } else {
                        receipts = receiptRepository.findBySocietyIdAndFinancialYearId(
                                societyId,
                                financialYearId);
                }

                // Optional flat filter
                if (flatId != null) {
                        receipts = receipts.stream()
                                        .filter(r -> flatId.equals(r.getFlatId()))
                                        .toList();
                }

                // Load all flats in a single query
                Set<Long> flatIds = receipts.stream()
                                .map(Receipt::getFlatId)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet());

                List<Flat> flats = flatRepository.findAllById(flatIds);

                Map<Long, Flat> flatMap = flats.stream()
                                .collect(Collectors.toMap(
                                                Flat::getId,
                                                Function.identity()));

                return receipts.stream().map(r -> {

                        ReceiptResponse dto = new ReceiptResponse();

                        dto.setId(r.getId());
                        dto.setReceiptNo(r.getReceiptNo());

                        if (r.getCreatedAt() != null) {
                                dto.setCreatedAt(r.getCreatedAt().toLocalDate());
                        }

                        dto.setFlatId(r.getFlatId());

                        dto.setMaintenanceAmount(r.getMaintenanceAmount());
                        dto.setInterestAmount(r.getInterestAmount());
                        dto.setDiscountAmount(r.getDiscountAmount());

                        dto.setTotalAmount(r.getTotalAmount());
                        dto.setTransactionId(r.getTransactionId());

                        // ================= FLAT DETAILS =================

                        Flat flat = flatMap.get(r.getFlatId());

                        if (flat != null) {

                                dto.setFlatNo(flat.getFlatNo());

                                if (flat.getOwner() != null) {

                                        dto.setMemberId(flat.getOwner().getId());
                                        dto.setMemberName(flat.getOwner().getName());
                                }
                        }

                        // ================= BILLING DETAILS =================

                        List<Billing> bills = billingRepository.findByReceiptId(r.getId());

                        if (!bills.isEmpty()) {

                                Billing bill = bills.get(0);

                                dto.setPaymentMode(bill.getPaymentMode());

                                // Prefer billing flat data if available
                                if (bill.getFlat() != null) {

                                        dto.setFlatNo(
                                                        bill.getFlat().getFlatNo());

                                        if (bill.getFlat().getOwner() != null) {

                                                dto.setMemberId(
                                                                bill.getFlat()
                                                                                .getOwner()
                                                                                .getId());

                                                dto.setMemberName(
                                                                bill.getFlat()
                                                                                .getOwner()
                                                                                .getName());
                                        }
                                }
                        }

                        return dto;

                }).toList();
        }

        public List<ReceiptDetailsResponse> getReceiptDetails(Long receiptId) {

                List<ReceiptDetailsResponse> response = new ArrayList<>();

                // Billing
                List<Billing> bills = billingRepository.findByReceiptId(receiptId);
                Receipt receipt = receiptRepository.findById(receiptId)
                                .orElseThrow(() -> new RuntimeException("Receipt not found"));

                if (!bills.isEmpty()) {

                        bills.forEach(b -> {
                                ReceiptDetailsResponse dto = new ReceiptDetailsResponse();

                                dto.setId(b.getId());
                                dto.setReceiptType("BILLING");
                                dto.setMonth(b.getMonth());
                                dto.setYear(b.getYear());
                                dto.setMaintenanceAmount(b.getMaintenanceAmount());
                                dto.setInterestAmount(b.getInterestAmount());
                                dto.setDiscountAmount(b.getDiscountAmount());
                                dto.setPenaltyAmount(b.getPenaltyAmount());
                                dto.setTotalAmount(b.getTotalAmount());
                                dto.setCreatedAt(receipt.getReceiptDate());
                                
                                if (b.getStatus() != null) {
                                        dto.setStatus(b.getStatus().name());
                                }

                                if (b.getFlat() != null) {
                                        dto.setFlatId(b.getFlat().getId());
                                        dto.setFlatNo(b.getFlat().getFlatNo());

                                        if (b.getFlat().getOwner() != null) {
                                                dto.setMemberId(b.getFlat().getOwner().getId());
                                                dto.setMemberName(b.getFlat().getOwner().getName());
                                        }
                                }

                                response.add(dto);
                        });
                }

                // Contribution
                List<Contribution> contributions = contributionRepository.findByReceiptId(receiptId);
                if(!contributions.isEmpty() )
                {
                        contributions.forEach(c -> {
                                ReceiptDetailsResponse dto = new ReceiptDetailsResponse();

                                dto.setId(c.getId());
                                dto.setReceiptType("CONTRIBUTION");
                                dto.setTotalAmount(c.getAmount());
                                dto.setMonth("");
                                dto.setYear(0);
                                dto.setStatus(c.getStatus().name());
                                dto.setname(c.getName());
                                dto.setCreatedAt(receipt.getReceiptDate());

                                response.add(dto);

                        });
                }

                // Sinking Fund
                List<SinkingFund> sinkingFunds = sinkingFundRepository.findByReceiptId(receiptId);

                sinkingFunds.forEach(s -> {
                        ReceiptDetailsResponse dto = new ReceiptDetailsResponse();

                        dto.setId(s.getId());
                        dto.setReceiptType("SINKING_FUND");
                        dto.setTotalAmount(s.getAmount());
                        dto.setCreatedAt(receipt.getReceiptDate());
                        dto.setStatus(s.getStatus().name());

                        response.add(dto);
                });

                return response;
        }
}

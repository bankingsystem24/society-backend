package com.society.backend.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.society.backend.entity.Billing;
import com.society.backend.entity.Flat;
import com.society.backend.entity.Member;
import com.society.backend.entity.Receipt;
import com.society.backend.entity.SinkingFund;
import com.society.backend.exception.ResourceNotFoundException;
import com.society.backend.gl.entity.Contribution;
import com.society.backend.gl.repository.ContributionRepository;
import com.society.backend.repository.BillingRepository;
import com.society.backend.repository.FlatRepository;
import com.society.backend.repository.MemberRepository;
import com.society.backend.repository.ReceiptRepository;
import com.society.backend.repository.SinkingFundRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberDashboardService {

    private final BillingRepository billingRepository;
    private final MemberRepository memberRepository;
    private final FlatRepository flatRepository;
    private final SinkingFundRepository sinkingFundRepository;
    private final ContributionRepository contributionRepository;
    private final ReceiptRepository receiptRepository;

public Map<String, Object> getDashboard(Long memberId,Long financialYearId) {

    Map<String, Object> map = new HashMap<>();

    Member member = memberRepository.findById(memberId)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Member not found"));

    List<Flat> flats = flatRepository.findBySociety_IdAndOwner_Id(member.getSociety().getId(), member.getId());

    double paidMaintenance = 0.0;
    double pendingMaintenance = 0.0;
    double paidFunds = 0.0;
    double pendingFunds = 0.0;
    double pendingContributions = 0.0;
    long receiptCount = 0;

    for (Flat flat : flats) {
        List<Billing> billings =
                billingRepository.findBySocietyIdAndFlatId(member.getSociety().getId(), flat.getId());

        for (Billing b : billings) {
            Double amt = b.getTotalAmount();
            if (amt == null) {
                amt = 0.0;
            }
            if ("PAID".equalsIgnoreCase(
                    String.valueOf(b.getStatus()))) {
                paidMaintenance += amt;
                receiptCount++;
            } else {
                pendingMaintenance += amt;
            }
        }

        List<SinkingFund> funds =
                sinkingFundRepository.findBySocietyIdAndFlat_Id(member.getSociety().getId(), flat.getId());

        for (SinkingFund b : funds) {
            Double amt = b.getAmount();
            if (amt == null) {
                amt = 0.0;
            }
            if ("PAID".equalsIgnoreCase(
                    String.valueOf(b.getStatus()))) {
                paidFunds += amt;
                receiptCount++;
            } else {
                pendingFunds += amt;
            }
        }

        List<Contribution> contributions =
                contributionRepository.findBySociety_IdAndFlat_Id(member.getSociety().getId(), flat.getId());

        for (Contribution b : contributions) {
            Double amt = b.getAmount();
            if (amt == null) {
                amt = 0.0;
            }
            if ("PAID".equalsIgnoreCase(
                    String.valueOf(b.getStatus()))) {
            } else {
                pendingContributions += amt;
            }
        }

    }

double paidContributions = 0.0;

List<Contribution> contributions =
        contributionRepository.findByMemberIdAndSocietyIdAndFinancialYearId(
                memberId,
                member.getSociety().getId(),
                financialYearId);

Map<Long, List<Contribution>> groupedContributions =
        contributions.stream()
                .filter(c -> c.getReceiptId() != null)
                .collect(Collectors.groupingBy(Contribution::getReceiptId));

for (Long receiptId : groupedContributions.keySet()) {

    Receipt receipt = receiptRepository.findById(receiptId).orElse(null);

    if (receipt != null) {
        paidContributions += receipt.getTotalAmount(); // replace with your receipt amount field
        receiptCount++;

    }
}

    map.put("paidMaintenance", paidMaintenance);
    map.put("pendingMaintenance", pendingMaintenance);
    map.put("paidFunds", paidFunds);
    map.put("pendingFunds", pendingFunds);
    map.put("receiptCount", receiptCount);
    map.put("paidContributions",paidContributions);
    map.put("pendingContributions",pendingContributions);

    return map;
}


}
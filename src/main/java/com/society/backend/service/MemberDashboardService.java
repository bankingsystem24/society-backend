package com.society.backend.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.society.backend.entity.Billing;
import com.society.backend.entity.Flat;
import com.society.backend.entity.Member;
import com.society.backend.exception.ResourceNotFoundException;
import com.society.backend.repository.BillingRepository;
import com.society.backend.repository.FlatRepository;
import com.society.backend.repository.MemberRepository;
@Service
public class MemberDashboardService {

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FlatRepository flatRepository;

public Map<String, Object> getDashboard(Long memberId) {

    Map<String, Object> map = new HashMap<>();

    Member member = memberRepository.findById(memberId)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Member not found"));

    List<Flat> flats = flatRepository.findBySociety_IdAndOwner_Id(member.getSociety().getId(), member.getId());

    double paid = 0.0;
    double pending = 0.0;
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

                paid += amt;
                receiptCount++;

            } else {

                pending += amt;
            }
        }
    }

    map.put("paidAmount", paid);
    map.put("pendingAmount", pending);
    map.put("receiptCount", receiptCount);

    return map;
}


}
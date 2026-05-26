package com.society.backend.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.society.backend.entity.Billing;
import com.society.backend.entity.Receipt;
import com.society.backend.repository.BillingRepository;
import com.society.backend.repository.ReceiptRepository;
@Service
public class MemberDashboardService {

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    public Map<String, Object> getDashboard(Long memberId) {

        Map<String, Object> map = new HashMap<>();

        // ⚠️ Since Receipt has NO memberId, fallback to society-level
        List<Receipt> receipts = receiptRepository.findAll();
        Long receiptCount = (long) receipts.size();

        // Billing also society-level safe fetch
        List<Billing> billings = billingRepository.findAll();

        double paid = 0.0;
        double pending = 0.0;

        for (Billing b : billings) {

            Double amt = b.getTotalAmount();
            if (amt == null) amt = 0.0;

            if (b.getStatus() != null &&
                b.getStatus().toString().equalsIgnoreCase("PAID")) {
                paid += amt;
            } else {
                pending += amt;
            }
        }

        map.put("paidAmount", paid);
        map.put("pendingAmount", pending);
        map.put("receiptCount", receiptCount);

        return map;
    }
}
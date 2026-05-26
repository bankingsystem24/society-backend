package com.society.backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.society.backend.dto.BillingResponse;
import com.society.backend.entity.Billing;
import com.society.backend.entity.Flat;
import com.society.backend.entity.Receipt;
import com.society.backend.enums.PaymentStatus;
import com.society.backend.repository.BillingRepository;
import com.society.backend.repository.FlatRepository;
import com.society.backend.repository.ReceiptRepository;

@Service
public class BillingService {

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private FlatRepository flatRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    // 🔥 AUTO GENERATE MONTHLY BILLS
    public String generateMonthlyBills(Long societyId, String month, int year) {

        List<Flat> flats = flatRepository.findBySociety_Id(societyId);

        int createdCount = 0;

        for (Flat flat : flats) {

            // ❌ prevent duplicate bill
            boolean exists = billingRepository
                    .existsByFlatIdAndMonthAndYear(
                            flat.getId(),
                            month,
                            year
                    );

            if (exists) {
                continue;
            }

            Billing bill = new Billing();

            bill.setSociety(flat.getSociety());
            bill.setFlat(flat);

            bill.setMonth(month);
            bill.setYear(year);

            // 💰 maintenance amount
            double amount = flat.getMaintenanceAmount() != null
                    ? flat.getMaintenanceAmount()
                    : 0.0;

            bill.setMaintenanceAmount(amount);
            bill.setPenaltyAmount(0.0);
            bill.setTotalAmount(amount);

            bill.setStatus(PaymentStatus.PENDING);

            bill.setDueDate(LocalDate.now().plusDays(10));
            bill.setCreatedDate(LocalDate.now());

            billingRepository.save(bill);

            createdCount++;
        }

        return createdCount + " bills generated successfully for "
                + month + " " + year;
    }

    // 📌 GET SOCIETY BILLS
    public List<Billing> getBySociety(Long societyId) {

        return billingRepository.findBySocietyId(societyId);
    }

    // 📌 GET PENDING BILLS
    public List<Billing> getPending(Long societyId) {

        return billingRepository.findBySocietyIdAndStatus(
                societyId,
                PaymentStatus.PENDING
        );
    }

    public List<BillingResponse> viewAllBills(
            Long societyId,
            Long flatId,
            Integer fromYear,
            String month,
            PaymentStatus status,
            Long memberId
    ) {

        List<Billing> bills = billingRepository.findBySocietyId(societyId);

        // ✅ FLAT FILTER
        if (flatId != null) {
            bills = bills.stream()
                    .filter(b -> b.getFlat() != null &&
                            b.getFlat().getId().equals(flatId))
                    .toList();
        }

        // ✅ MONTH FILTER
        if (month != null && !month.isEmpty()) {
            bills = bills.stream()
                    .filter(b -> b.getMonth() != null &&
                            b.getMonth().equalsIgnoreCase(month))
                    .toList();
        }

        // ✅ STATUS FILTER (FIXED)
        if (status != null) {
            bills = bills.stream()
                    .filter(b -> b.getStatus() != null &&
                            b.getStatus().equals(status))
                    .toList();
        }

        if (memberId != null) {
            bills = bills.stream()
                    .filter(b -> b.getFlat() != null &&
                            b.getFlat().getOwner() != null &&
                            b.getFlat().getOwner().getId().equals(memberId))
                    .toList();
            }

        // ✅ FINANCIAL YEAR FILTER
        if (fromYear != null) {

            int toYear = fromYear + 1;

            bills = bills.stream()
                    .filter(bill -> {

                        String m = bill.getMonth().toUpperCase();

                        boolean aprToDec =
                                m.equals("APRIL") || m.equals("MAY") ||
                                m.equals("JUNE") || m.equals("JULY") ||
                                m.equals("AUGUST") || m.equals("SEPTEMBER") ||
                                m.equals("OCTOBER") || m.equals("NOVEMBER") ||
                                m.equals("DECEMBER");

                        if (aprToDec) {
                            return bill.getYear() == fromYear;
                        }

                        return bill.getYear() == toYear;
                    })
                    .toList();
        }

        return bills.stream().map(b -> {

        BillingResponse dto = new BillingResponse();

        dto.setId(b.getId());
        dto.setMonth(b.getMonth());
        dto.setYear(b.getYear());

        dto.setMaintenanceAmount(b.getMaintenanceAmount());
        dto.setPenaltyAmount(b.getPenaltyAmount());
        dto.setTotalAmount(b.getTotalAmount());

        dto.setStatus(b.getStatus().name());

        dto.setFlatId(b.getFlat().getId());
        dto.setFlatNo(b.getFlat().getFlatNo());

        // ✅ MEMBER
        if (b.getFlat().getOwner() != null) {
            dto.setMemberId(b.getFlat().getOwner().getId());
            dto.setMemberName(b.getFlat().getOwner().getName());
        }

        // ✅ RECEIPT
        dto.setReceiptId(b.getReceiptId());

        if (b.getReceiptId() != null) {

            Receipt receipt = receiptRepository
                    .findById(b.getReceiptId())
                    .orElse(null);

            if (receipt != null) {
                dto.setReceiptNo(receipt.getReceiptNo());
            }
        }

        return dto;

    }).toList();

    }

    public String payBills(List<Long> billIds, String paymentMode) {

        List<Billing> bills = billingRepository.findAllById(billIds);

        for (Billing bill : bills) {
            bill.setStatus(PaymentStatus.PAID);
            bill.setPaidDate(LocalDate.now());
            bill.setPaymentMode(paymentMode);
        }

        billingRepository.saveAll(bills);

        return "Bills paid successfully";
    }

public List<Billing> getBillsByFlatIds(List<Long> flatIds) {
    return billingRepository.findByFlatIdIn(flatIds);
}

}
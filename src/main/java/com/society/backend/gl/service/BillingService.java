package com.society.backend.gl.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.society.backend.dto.BillingResponse;
import com.society.backend.entity.Billing;
import com.society.backend.entity.Flat;
import com.society.backend.entity.Receipt;
import com.society.backend.enums.PaymentStatus;
import com.society.backend.gl.service.JournalService;
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

    @Autowired
    private JournalService journalService;

    // =====================================================
    // GENERATE MONTHLY BILLS
    // =====================================================

    @Transactional
    public String generateMonthlyBills(
            Long societyId,
            String month,
            int year) {

        List<Flat> flats = flatRepository.findBySociety_Id(societyId);

        int createdCount = 0;

        for (Flat flat : flats) {

            // ================= DUPLICATE CHECK =================

            boolean exists = billingRepository
                    .existsByFlatIdAndMonthAndYear(
                            flat.getId(),
                            month,
                            year
                    );

            if (exists) {
                continue;
            }

            // ================= BILL CREATION =================

            Billing bill = new Billing();

            bill.setSociety(flat.getSociety());

            bill.setFlat(flat);

            bill.setMonth(month);

            bill.setYear(year);

            double amount = flat.getMaintenanceAmount() != null
                    ? flat.getMaintenanceAmount()
                    : 0.0;

            bill.setMaintenanceAmount(amount);

            bill.setPenaltyAmount(0.0);

            bill.setTotalAmount(amount);

            bill.setStatus(PaymentStatus.PENDING);

            bill.setDueDate(LocalDate.now().plusDays(10));

            bill.setCreatedDate(LocalDate.now());

            Billing savedBill = billingRepository.save(bill);


            // ================= ACCOUNTING ENTRY =================

            Long memberId = flat.getOwner() != null
                    ? flat.getOwner().getId()
                    : null;

                    

            journalService.createMaintenanceBillEntry(
                    savedBill.getId(),
                    memberId,
                    amount,
                    societyId
            );

            createdCount++;
        }

        return createdCount + " bills generated successfully for "
                + month + " " + year;
    }

    // =====================================================
    // GET BILLS BY SOCIETY
    // =====================================================

    public List<Billing> getBySociety(Long societyId) {

        return billingRepository.findBySocietyId(societyId);
    }

    // =====================================================
    // GET PENDING BILLS
    // =====================================================

    public List<Billing> getPending(Long societyId) {

        return billingRepository.findBySocietyIdAndStatus(
                societyId,
                PaymentStatus.PENDING
        );
    }

    // =====================================================
    // VIEW ALL BILLS
    // =====================================================

    public List<BillingResponse> viewAllBills(
            Long societyId,
            Long flatId,
            Integer fromYear,
            String month,
            PaymentStatus status,
            Long memberId
    ) {

        List<Billing> bills = billingRepository.findBySocietyId(societyId);

        // ================= FLAT FILTER =================

        if (flatId != null) {

            bills = bills.stream()
                    .filter(b -> b.getFlat() != null
                            && b.getFlat().getId().equals(flatId))
                    .toList();
        }

        // ================= MONTH FILTER =================

        if (month != null && !month.isEmpty()) {

            bills = bills.stream()
                    .filter(b -> b.getMonth() != null
                            && b.getMonth().equalsIgnoreCase(month))
                    .toList();
        }

        // ================= STATUS FILTER =================

        if (status != null) {

            bills = bills.stream()
                    .filter(b -> b.getStatus() != null
                            && b.getStatus().equals(status))
                    .toList();
        }

        // ================= MEMBER FILTER =================

        if (memberId != null) {

            bills = bills.stream()
                    .filter(b -> b.getFlat() != null
                            && b.getFlat().getOwner() != null
                            && b.getFlat().getOwner().getId().equals(memberId))
                    .toList();
        }

        // ================= FINANCIAL YEAR FILTER =================

        if (fromYear != null) {

            int toYear = fromYear + 1;

            bills = bills.stream()
                    .filter(bill -> {

                        String m = bill.getMonth().toUpperCase();

                        boolean aprToDec =
                                m.equals("APRIL") ||
                                m.equals("MAY") ||
                                m.equals("JUNE") ||
                                m.equals("JULY") ||
                                m.equals("AUGUST") ||
                                m.equals("SEPTEMBER") ||
                                m.equals("OCTOBER") ||
                                m.equals("NOVEMBER") ||
                                m.equals("DECEMBER");

                        if (aprToDec) {
                            return bill.getYear() == fromYear;
                        }

                        return bill.getYear() == toYear;
                    })
                    .toList();
        }

        // ================= DTO MAPPING =================

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

            // MEMBER

            if (b.getFlat().getOwner() != null) {

                dto.setMemberId(b.getFlat().getOwner().getId());

                dto.setMemberName(b.getFlat().getOwner().getName());
            }

            // RECEIPT

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

    // =====================================================
    // PAY BILLS
    // =====================================================

    @Transactional
    public String payBills(
            List<Long> billIds,
            String paymentMode) {

        List<Billing> bills = billingRepository.findAllById(billIds);

        if (bills.isEmpty()) {
            return "No bills found";
        }

        double totalAmount = 0;

        for (Billing bill : bills) {

            if (bill.getStatus() == PaymentStatus.PAID) {
                continue;
            }

            bill.setStatus(PaymentStatus.PAID);

            bill.setPaidDate(LocalDate.now());

            bill.setPaymentMode(paymentMode);

            totalAmount += bill.getTotalAmount();
        }

        // ================= RECEIPT CREATION =================

        Receipt receipt = new Receipt();

        receipt.setReceiptNo("RCPT-" + System.currentTimeMillis());

        receipt.setReceiptDate(LocalDate.now());

        receipt.setPaymentMode(paymentMode);

        receipt.setTotalAmount(totalAmount);

        Billing first = bills.get(0);

        Long societyId = first.getSociety().getId();

        Long memberId = first.getFlat().getOwner() != null
                ? first.getFlat().getOwner().getId()
                : null;

        receipt.setSocietyId(societyId);

        receipt.setFlatId(first.getFlat().getId());

        Receipt savedReceipt = receiptRepository.save(receipt);

        // ================= ACCOUNTING ENTRY =================

        journalService.createReceiptEntry(
                savedReceipt.getId(),
                memberId,
                totalAmount,
                paymentMode,
                societyId
        );

        // ================= UPDATE BILLS =================

        for (Billing bill : bills) {

            bill.setReceiptId(savedReceipt.getId());
        }

        billingRepository.saveAll(bills);

        return "Bills paid successfully";
    }

    // =====================================================
    // GET BILLS BY FLAT IDS
    // =====================================================

    public List<Billing> getBillsByFlatIds(List<Long> flatIds) {

        List<Billing> bills =
                billingRepository.findByFlatIdIn(flatIds);

        bills.forEach(bill -> {

            if (bill.getReceiptId() != null) {

                receiptRepository.findById(bill.getReceiptId())
                        .ifPresent(receipt -> {

                            bill.setReceiptNo(
                                    receipt.getReceiptNo()
                            );
                        });
            }
        });

        return bills;
    }
}
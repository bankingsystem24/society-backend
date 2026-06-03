package com.society.backend.gl.service;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.society.backend.dto.BillingResponse;
import com.society.backend.entity.Billing;
import com.society.backend.entity.Flat;
import com.society.backend.entity.Receipt;
import com.society.backend.gl.entity.SocietyBillingPolicy;
import com.society.backend.enums.PaymentStatus;
import com.society.backend.gl.repository.SocietyBillingPolicyRepository;
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

    @Autowired
    private SocietyBillingPolicyRepository societyBillingPolicyRepository;

    // =====================================================
    // GENERATE MONTHLY BILLS
    // =====================================================

@Transactional
public String generateMonthlyBills(
        Long societyId,
        String month,
        int year) {

    List<Flat> flats = flatRepository.findBySociety_Id(societyId);

    // Fetch policy for society
    SocietyBillingPolicy policy = societyBillingPolicyRepository
            .findBySocietyId(societyId)
            .orElseThrow(() ->
                    new RuntimeException("Billing policy not found"));

    int createdCount = 0;

    // Convert month name to Month enum
    Month billingMonth = Month.valueOf(month.toUpperCase());

    // First day of billing month
    LocalDate billDate = LocalDate.of(year, billingMonth, 1);

    // Due date from policy
    LocalDate dueDate = LocalDate.of(
            year,
            billingMonth,
            policy.getBillingDay()
    );

    LocalDate finalDueDate = dueDate.plusDays(
        policy.getGraceDays() != null ? policy.getGraceDays() : 0
);


    for (Flat flat : flats) {

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

        double amount = flat.getMaintenanceAmount() != null
                ? flat.getMaintenanceAmount()
                : 0.0;

        bill.setMaintenanceAmount(amount);
        bill.setPenaltyAmount(0.0);
        bill.setTotalAmount(amount);
        bill.setStatus(PaymentStatus.PENDING);

        // Set bill date and due date
        bill.setCreatedDate(LocalDate.of(year, billingMonth, 1));
        bill.setDueDate(finalDueDate);

        Billing savedBill = billingRepository.save(bill);

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

        if (month != null && !month.isBlank()) {

            bills = bills.stream()
                    .filter(b -> b.getMonth() != null
                            && b.getMonth().equalsIgnoreCase(month))
                    .toList();
        }

        // ================= STATUS FILTER =================

        if (status != null) {

            bills = bills.stream()
                    .filter(b -> b.getStatus() == status)
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

                        if (bill.getMonth() == null) {
                            return false;
                        }

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

        // Fetch policy once instead of every bill
        SocietyBillingPolicy policy =
                societyBillingPolicyRepository
                        .findBySociety_Id(societyId)
                        .orElse(null);

        // ================= DTO MAPPING =================

        return bills.stream().map(b -> {

            double interest = b.getInterestAmount() != null
                    ? b.getInterestAmount()
                    : 0.0;

            // Calculate live interest for pending bills
            if (b.getStatus() == PaymentStatus.PENDING
                    && policy != null
                    && b.getDueDate() != null
                    && b.getMaintenanceAmount() != null) {

                LocalDate penaltyStart =
                        b.getDueDate().plusDays(
                                policy.getGraceDays() != null
                                        ? policy.getGraceDays()
                                        : 0
                        );

                if (LocalDate.now().isAfter(penaltyStart)) {

                    long monthsLate = ChronoUnit.MONTHS.between(
                            penaltyStart.withDayOfMonth(1),
                            LocalDate.now().withDayOfMonth(1)
                    );

                    monthsLate = Math.max(1, monthsLate);

                    long periods = 0;

                    if (policy.getInterestType() != null) {

                        switch (policy.getInterestType()) {

                            case MONTHLY:
                                periods = monthsLate;
                                break;

                            case QUARTERLY:
                                periods = monthsLate / 3;
                                break;

                            case HALF_YEARLY:
                                periods = monthsLate / 6;
                                break;

                            case YEARLY:
                                periods = monthsLate / 12;
                                break;
                        }
                    }

                    interest =
                            b.getMaintenanceAmount()
                                    * policy.getInterestRate()
                                    * periods
                                    / 1200.0;
                }
            }

            BillingResponse dto = new BillingResponse();

            dto.setId(b.getId());
            dto.setMonth(b.getMonth());
            dto.setYear(b.getYear());
            dto.setMaintenanceAmount(b.getMaintenanceAmount());
            dto.setPenaltyAmount(b.getPenaltyAmount());
            dto.setInterestAmount(interest);
            dto.setDiscountAmount(b.getDiscountAmount());
            dto.setDueDate(b.getDueDate());

            double totalAmount =
                    (b.getMaintenanceAmount() != null ? b.getMaintenanceAmount() : 0.0)
                            + (b.getPenaltyAmount() != null ? b.getPenaltyAmount() : 0.0)
                            + interest
                            - (b.getDiscountAmount() != null ? b.getDiscountAmount() : 0.0);

            dto.setTotalAmount(totalAmount);

            dto.setStatus(
                    b.getStatus() != null
                            ? b.getStatus().name()
                            : null
            );

            // Flat Details
            if (b.getFlat() != null) {

                dto.setFlatId(b.getFlat().getId());
                dto.setFlatNo(b.getFlat().getFlatNo());

                // Member Details

                if (b.getFlat().getOwner() != null) {

                    dto.setMemberId(
                            b.getFlat().getOwner().getId()
                    );

                    dto.setMemberName(
                            b.getFlat().getOwner().getName()
                    );
                }
            }

            // Receipt Details

            dto.setReceiptId(b.getReceiptId());

            if (b.getReceiptId() != null) {

                receiptRepository.findById(b.getReceiptId())
                        .ifPresent(receipt ->
                                dto.setReceiptNo(receipt.getReceiptNo()));
            }

            return dto;

        }).toList();
    }
    // =====================================================
    // PAY BILLS
    // =====================================================

@Transactional
public String payBills(List<Long> billIds, String paymentMode) {

    List<Billing> bills = billingRepository.findAllById(billIds);

    if (bills.isEmpty()) {
        return "No bills found";
    }

    double totalAmount = 0.0;

    double maintenanceAmount = 0.0;
    double interestAmount = 0.0;
    double discountAmount = 0.0;

    for (Billing bill : bills) {

        if (bill.getStatus() == PaymentStatus.PAID) {
            continue;
        }

        bill.setStatus(PaymentStatus.PAID);
        bill.setPaidDate(LocalDate.now());
        bill.setPaymentMode(paymentMode);

        double maintenance = bill.getMaintenanceAmount() != null ? bill.getMaintenanceAmount() : 0.0;
        double discount = bill.getDiscountAmount() != null ? bill.getDiscountAmount() : 0.0;

        // ================= INTEREST CALCULATION =================
        double interest = 0.0;

        SocietyBillingPolicy policy =
                societyBillingPolicyRepository
                        .findBySociety_Id(bill.getSociety().getId())
                        .orElse(null);

        if (policy != null && bill.getDueDate() != null) {

            LocalDate penaltyStart =
                    bill.getDueDate().plusDays(policy.getGraceDays());

            if (LocalDate.now().isAfter(penaltyStart)) {

                long monthsLate = ChronoUnit.MONTHS.between(
                        penaltyStart.withDayOfMonth(1),
                        LocalDate.now().withDayOfMonth(1)
                );

                monthsLate = Math.max(1, monthsLate);

                interest =
                        maintenance
                                * policy.getInterestRate()
                                * monthsLate
                                / 1200.0;
            }
        }

        // ================= SET BILL VALUES =================
        bill.setInterestAmount(interest);

        double total = maintenance + interest - discount;

        bill.setTotalAmount(total);

        bill.setReceiptId(null); // set later

        // ================= AGGREGATE =================
        maintenanceAmount += maintenance;
        interestAmount += interest;
        discountAmount += discount;
        totalAmount += total;
    }

    // ================= RECEIPT =================

    Billing first = bills.get(0);

    Receipt receipt = new Receipt();
    receipt.setReceiptNo("RCPT-" + System.currentTimeMillis());
    receipt.setReceiptDate(LocalDate.now());
    receipt.setPaymentMode(paymentMode);

    receipt.setMaintenanceAmount(maintenanceAmount);
    receipt.setInterestAmount(interestAmount);
    receipt.setDiscountAmount(discountAmount);
    receipt.setTotalAmount(totalAmount);

    receipt.setSocietyId(first.getSociety().getId());
    receipt.setFlatId(first.getFlat().getId());

    Receipt savedReceipt = receiptRepository.save(receipt);

    // ================= UPDATE BILLS WITH RECEIPT =================

    for (Billing bill : bills) {
        bill.setReceiptId(savedReceipt.getId());
    }

    billingRepository.saveAll(bills);

    // ================= JOURNAL =================

    Long memberId =
            first.getFlat().getOwner() != null
                    ? first.getFlat().getOwner().getId()
                    : null;

    journalService.createReceiptEntry(
            savedReceipt.getId(),
            memberId,
            maintenanceAmount,
            interestAmount,
            discountAmount,
            totalAmount,
            paymentMode,
            first.getSociety().getId()
    );

    return "Bills paid successfully";
}
    // =====================================================
    // GET BILLS BY FLAT IDS
    // =====================================================

    public List<Billing> getBillsByFlatIds(List<Long> flatIds) {

        List<Billing> bills =
                billingRepository.findByFlatIdIn(flatIds);

        bills.forEach(bill -> {

            // Receipt No

            if (bill.getReceiptId() != null) {

                receiptRepository.findById(bill.getReceiptId())
                        .ifPresent(receipt ->
                                bill.setReceiptNo(
                                        receipt.getReceiptNo()
                                ));
            }

            // Interest Calculation

            double interest =
                    bill.getInterestAmount() != null
                            ? bill.getInterestAmount()
                            : 0.0;

            if (bill.getStatus() == PaymentStatus.PENDING
                    && bill.getDueDate() != null
                    && bill.getMaintenanceAmount() != null
                    && bill.getSociety() != null) {

                SocietyBillingPolicy policy =
                        societyBillingPolicyRepository
                                .findBySociety_Id(
                                        bill.getSociety().getId()
                                )
                                .orElse(null);

                if (policy != null) {

                    LocalDate penaltyStart =
                            bill.getDueDate().plusDays(
                                    policy.getGraceDays() != null
                                            ? policy.getGraceDays()
                                            : 0
                            );

                    if (LocalDate.now().isAfter(penaltyStart)) {

                        long monthsLate = ChronoUnit.MONTHS.between(
                                penaltyStart.withDayOfMonth(1),
                                LocalDate.now().withDayOfMonth(1)
                        );

                        monthsLate = Math.max(1, monthsLate);

                        long periods = 0;

                        if (policy.getInterestType() != null) {

                            switch (policy.getInterestType()) {

                                case MONTHLY:
                                    periods = monthsLate;
                                    break;

                                case QUARTERLY:
                                    periods = monthsLate / 3;
                                    break;

                                case HALF_YEARLY:
                                    periods = monthsLate / 6;
                                    break;

                                case YEARLY:
                                    periods = monthsLate / 12;
                                    break;
                            }
                        }

                        interest =
                                bill.getMaintenanceAmount()
                                        * policy.getInterestRate()
                                        * periods
                                        / 1200.0;
                    }
                }
            }

            bill.setInterestAmount(interest);

            double total =
                    (bill.getMaintenanceAmount() != null
                            ? bill.getMaintenanceAmount()
                            : 0.0)
                    + (bill.getPenaltyAmount() != null
                            ? bill.getPenaltyAmount()
                            : 0.0)
                    + interest
                    - (bill.getDiscountAmount() != null
                            ? bill.getDiscountAmount()
                            : 0.0);

            bill.setTotalAmount(total);
        });

        return bills;
    }


}
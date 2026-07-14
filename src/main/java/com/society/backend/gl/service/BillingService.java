package com.society.backend.gl.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.society.backend.dto.BillGenerateRequest;
import com.society.backend.dto.BillingReceiptRequest;
import com.society.backend.dto.BillingResponse;
import com.society.backend.dto.InterestCalculationRequest;
import com.society.backend.dto.InterestCalculationResponse;
import com.society.backend.entity.Billing;
import com.society.backend.entity.Flat;
import com.society.backend.entity.Member;
import com.society.backend.entity.Receipt;
import com.society.backend.gl.entity.DiscountPolicy;
import com.society.backend.gl.entity.SocietyBillingPolicy;
import com.society.backend.enums.PaymentStatus;
import com.society.backend.gl.repository.DiscountPolicyRepository;
import com.society.backend.gl.repository.SocietyBillingPolicyRepository;
import com.society.backend.repository.BillingRepository;
import com.society.backend.repository.FlatRepository;
import com.society.backend.repository.ReceiptRepository;

@Service
public class BillingService {

        private final DiscountPolicyRepository discountPolicyRepository;

        public BillingService(BillingRepository billingRepository,
                        FlatRepository flatRepository,
                        ReceiptRepository receiptRepository,
                        JournalService journalService,
                        SocietyBillingPolicyRepository societyBillingPolicyRepository,
                        DiscountPolicyRepository discountPolicyRepository) {
                this.billingRepository = billingRepository;
                this.flatRepository = flatRepository;
                this.receiptRepository = receiptRepository;
                this.journalService = journalService;
                this.societyBillingPolicyRepository = societyBillingPolicyRepository;
                this.discountPolicyRepository = discountPolicyRepository;
        }

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
                        int year,
                        Long createdBy,
                        Long financialYearId,
                        Integer glReceivable,
                        Integer glCreditAccount) {

                List<Flat> flats = flatRepository.findBySociety_Id(societyId);

                if (flats == null || flats.isEmpty()) {
                        return "No flats found for society";
                }

                Optional<SocietyBillingPolicy> policyOpt = societyBillingPolicyRepository
                                .findBySociety_IdAndFinancialYearId(societyId, financialYearId);

                SocietyBillingPolicy policy = policyOpt.orElse(null);

                Month billingMonth;
                try {
                        billingMonth = Month.valueOf(month.toUpperCase());
                } catch (Exception e) {
                        throw new RuntimeException("Invalid month: " + month);
                }

                LocalDate interestStart = LocalDate.of(year, billingMonth, 1);

                // Apply policy only if it exists
                if (policy != null) {
                        switch (policy.getInterestType()) {

                                case MONTHLY:
                                        interestStart = interestStart.plusMonths(1);
                                        break;

                                case QUARTERLY:
                                        interestStart = interestStart.plusMonths(3);
                                        break;

                                case HALF_YEARLY:
                                        interestStart = interestStart.plusMonths(6);
                                        break;

                                case YEARLY:
                                        interestStart = interestStart.plusMonths(12);
                                        break;
                        }
                }

                LocalDate finalDueDate = interestStart;

                int createdCount = 0;

                for (Flat flat : flats) {

                        if (flat == null)
                                continue;
                        if (flat.getId() == null)
                                continue;
                        if (flat.getOwner() == null || flat.getOwner().getId() == null)
                                continue;

                        boolean exists = billingRepository.existsByFlatIdAndMonthAndYear(
                                        flat.getId(),
                                        month,
                                        year);

                        if (exists)
                                continue;

                        double amount = flat.getMaintenanceAmount() != null
                                        ? flat.getMaintenanceAmount()
                                        : 0.0;

                        amount = Math.round(amount);
                        Billing bill = new Billing();
                        bill.setSociety(flat.getSociety());
                        bill.setFlat(flat);
                        bill.setMonth(month);
                        bill.setYear(year);
                        bill.setMaintenanceAmount(amount);
                        bill.setPenaltyAmount(0.0);
                        bill.setTotalAmount(amount);
                        bill.setStatus(PaymentStatus.PENDING);
                        bill.setCreatedDate(LocalDate.of(year, billingMonth, 1));
                        bill.setDueDate(finalDueDate);
                        bill.setFinancialYearId(financialYearId);

                        Billing savedBill = billingRepository.save(bill);

                        Member member = flat.getOwner();

                        // ================= IMPORTANT FIX =================
                        // DO NOT hide journal errors
                        try {
                                Long journalId = journalService.createMaintenanceBillEntry(
                                                savedBill.getId(),
                                                member,
                                                amount,
                                                societyId,
                                                createdBy,
                                                flat.getId(),
                                                financialYearId,
                                                glReceivable,
                                                glCreditAccount);

                                if (journalId == null) {
                                        throw new RuntimeException("Journal not created for bill " + savedBill.getId());
                                }

                        } catch (Exception e) {
                                throw new RuntimeException(
                                                "Journal failed for billId=" + savedBill.getId()
                                                                + " -> " + e.getMessage());
                        }

                        createdCount++;
                }

                return createdCount + " bills generated successfully for " + month + " " + year;
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
                                PaymentStatus.PENDING);
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
                        Long memberId,
                        Long financialYearId) {

                List<Billing> bills = billingRepository.findBySocietyIdAndFinancialYearId(societyId, financialYearId);

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

                                                boolean aprToDec = m.equals("APRIL") ||
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
                SocietyBillingPolicy policy = societyBillingPolicyRepository
                                .findBySociety_IdAndFinancialYearId(societyId, financialYearId)
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

                                LocalDate interestStart = b.getCreatedDate();

                                switch (policy.getInterestType()) {

                                        case MONTHLY:
                                                interestStart = interestStart.plusMonths(1);
                                                break;

                                        case QUARTERLY:
                                                interestStart = interestStart.plusMonths(3);
                                                break;

                                        case HALF_YEARLY:
                                                interestStart = interestStart.plusMonths(6);
                                                break;

                                        case YEARLY:
                                                interestStart = interestStart.plusMonths(12);
                                                break;
                                }

                                if (LocalDate.now().isAfter(interestStart)) {

                                        long daysLate = ChronoUnit.DAYS.between(b.getCreatedDate(), LocalDate.now());

                                        interest = Math.round(
                                                        b.getMaintenanceAmount()
                                                                        * policy.getInterestRate()
                                                                        * daysLate
                                                                        / (365.0 * 100));
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

                        double totalAmount = (b.getMaintenanceAmount() != null ? b.getMaintenanceAmount() : 0.0)
                                        + (b.getPenaltyAmount() != null ? b.getPenaltyAmount() : 0.0)
                                        + interest
                                        - (b.getDiscountAmount() != null ? b.getDiscountAmount() : 0.0);

                        dto.setTotalAmount(totalAmount);
                        dto.setFinancialYearId(financialYearId);
                        dto.setStatus(b.getStatus() != null ? b.getStatus().name() : null);

                        // Flat Details
                        if (b.getFlat() != null) {

                                dto.setFlatId(b.getFlat().getId());
                                dto.setFlatNo(b.getFlat().getFlatNo());

                                // Member Details

                                if (b.getFlat().getOwner() != null) {

                                        dto.setMemberId(
                                                        b.getFlat().getOwner().getId());

                                        dto.setMemberName(
                                                        b.getFlat().getOwner().getName());
                                }
                        }

                        // Receipt Details

                        dto.setReceiptId(b.getReceiptId());

                        if (b.getReceiptId() != null) {

                                receiptRepository.findById(b.getReceiptId())
                                                .ifPresent(receipt -> dto.setReceiptNo(receipt.getReceiptNo()));
                        }
                        dto.setTransactionId(b.getTransactionId());
                        return dto;

                }).toList();
        }
        // =====================================================
        // PAY BILLS
        // =====================================================

        @Transactional
        public String payBills(List<Long> billIds, String paymentMode, LocalDate paymentDate, Long financialYearId,
                        String transactionId,
                        Integer glReceivable, Integer glCreditAccount, Integer glCashInHand, Integer glBankAccount,
                        Integer glInterestIncome, Integer glDiscount, Double interestAmount, Double discountAmount,
                        Integer selectedCount) {

                List<Billing> bills = billingRepository.findAllById(billIds);

                if (bills.isEmpty()) {
                        return "No bills found";
                }

                Billing first = bills.get(0);

                Long flatId = first.getFlat().getId();
                Long societyId = first.getSociety().getId();

                // ================= VALIDATION =================

                for (Billing bill : bills) {

                        if (!bill.getFlat().getId().equals(flatId)) {
                                throw new RuntimeException(
                                                "Selected bills must belong to same flat");
                        }

                        if (!bill.getSociety().getId().equals(societyId)) {
                                throw new RuntimeException(
                                                "Selected bills must belong to same society");
                        }
                }

                double totalAmount = 0.0;
                double maintenanceAmount = 0.0;
                // double interestAmount = 0.0;
                // double discountAmount = 0.0;

                boolean hasUnpaidBills = false;

                // ================= BILL PROCESSING =================

                List<Billing> paidBills = new ArrayList<>();

                SocietyBillingPolicy policy = societyBillingPolicyRepository
                                .findBySociety_IdAndFinancialYearId(societyId, financialYearId)
                                .orElse(null);

                for (Billing bill : bills) {

                        // Skip already paid bills
                        if (bill.getStatus() == PaymentStatus.PAID) {
                                continue;
                        }

                        hasUnpaidBills = true;

                        bill.setStatus("CASH".equals(paymentMode) ? PaymentStatus.PAID : PaymentStatus.SUBMITTED);

                        bill.setPaidDate(paymentDate);
                        bill.setPaymentMode(paymentMode);
                        bill.setTransactionId(transactionId);

                        double maintenance = bill.getMaintenanceAmount() != null
                                        ? bill.getMaintenanceAmount()
                                        : 0.0;

                        double discount = bill.getDiscountAmount() != null
                                        ? bill.getDiscountAmount()
                                        : 0.0;

                        double interest = 0.0;

                        if (policy != null && bill.getDueDate() != null) {

                                LocalDate interestStart = bill.getDueDate();

                                switch (policy.getInterestType()) {

                                        case MONTHLY:
                                                interestStart = interestStart.plusMonths(1);
                                                break;

                                        case QUARTERLY:
                                                interestStart = interestStart.plusMonths(3);
                                                break;

                                        case HALF_YEARLY:
                                                interestStart = interestStart.plusMonths(6);
                                                break;

                                        case YEARLY:
                                                interestStart = interestStart.plusMonths(12);
                                                break;
                                }

                                if (LocalDate.now().isAfter(interestStart)) {

                                        long monthsLate = ChronoUnit.MONTHS.between(
                                                        interestStart.withDayOfMonth(1),
                                                        LocalDate.now().withDayOfMonth(1));

                                        monthsLate = Math.max(0, monthsLate);

                                        interest = maintenance
                                                        * policy.getInterestRate()
                                                        * monthsLate
                                                        / 1200.0;
                                }
                        }

                        bill.setInterestAmount(interest);

                        double total = maintenance + interest - discount;

                        bill.setTotalAmount(total);

                        maintenanceAmount += maintenance;
                        interestAmount += interest;
                        discountAmount += discount;
                        totalAmount += total;
                        // bill.setInterestAmount(interestAmount);
                        // bill.setDiscountAmount(discountAmount);

                        // Track only bills paid in this transaction
                        paidBills.add(bill);
                }

                if (!hasUnpaidBills) {
                        return "All selected bills are already paid";
                }

                totalAmount += interestAmount - discountAmount;
                // ================= RECEIPT =================

                Receipt receipt = new Receipt();

                receipt.setReceiptDate(LocalDate.now());
                receipt.setPaymentMode(paymentMode);
                receipt.setStatus("CASH".equals(paymentMode) ? PaymentStatus.PAID : PaymentStatus.SUBMITTED);

                receipt.setMaintenanceAmount(maintenanceAmount);
                receipt.setInterestAmount(interestAmount);
                receipt.setDiscountAmount(discountAmount);
                receipt.setTotalAmount(totalAmount);

                receipt.setSocietyId(societyId);
                receipt.setFlatId(flatId);
                receipt.setFinancialYearId(financialYearId);
                receipt.setTransactionId(transactionId);

                Receipt savedReceipt = receiptRepository.save(receipt);

                savedReceipt.setReceiptNo("RCPT-" + LocalDate.now().getYear() + "-" + savedReceipt.getId());

                savedReceipt = receiptRepository.save(savedReceipt);

                // ================= UPDATE BILLS =================

                for (Billing bill : paidBills) {
                        bill.setReceiptId(savedReceipt.getId());
                }

                billingRepository.saveAll(paidBills);

                // ================= JOURNAL =================

                Long memberId = null;

                if (first.getFlat() != null &&
                                first.getFlat().getOwner() != null) {

                        memberId = first.getFlat().getOwner().getId();
                }

                if (totalAmount > 0 && "CASH".equals(paymentMode)) {

                        journalService.createReceiptEntry(
                                        savedReceipt.getId(),
                                        memberId,
                                        maintenanceAmount,
                                        interestAmount,
                                        discountAmount,
                                        totalAmount,
                                        paymentMode,
                                        societyId,
                                        0L,
                                        flatId,
                                        financialYearId,
                                        glReceivable,
                                        glCreditAccount,
                                        glCashInHand,
                                        glBankAccount,
                                        glInterestIncome,
                                        glDiscount);
                }

                return "Bills paid successfully";
        }

        // =====================================================
        // GET BILLS BY FLAT IDS
        // =====================================================

        public List<Billing> getBillsByFlatIds(List<Long> flatIds, Long societyId, Long financialYearId) {

                List<Billing> bills = billingRepository.findByFlatIdInAndSocietyIdAndFinancialYearId(flatIds, societyId,
                                financialYearId);

                bills.forEach(bill -> {

                        if (bill.getReceiptId() != null) {

                                receiptRepository.findById(bill.getReceiptId())
                                                .ifPresent(receipt -> {
                                                        bill.setReceiptNo(receipt.getReceiptNo());
                                                        bill.setTransactionId(receipt.getTransactionId());
                                                });
                        }

                        double interest = bill.getInterestAmount() != null ? bill.getInterestAmount() : 0.0;

                        if (bill.getStatus() == PaymentStatus.PENDING
                                        && bill.getDueDate() != null
                                        && bill.getMaintenanceAmount() != null
                                        && bill.getSociety() != null) {

                                SocietyBillingPolicy policy = societyBillingPolicyRepository
                                                .findBySociety_IdAndFinancialYearId(
                                                                bill.getSociety().getId(), financialYearId)
                                                .orElse(null);

                                if (policy != null) {

                                        LocalDate interestStart = bill.getCreatedDate();

                                        switch (policy.getInterestType()) {

                                                case MONTHLY:
                                                        interestStart = interestStart.plusMonths(1);
                                                        break;

                                                case QUARTERLY:
                                                        interestStart = interestStart.plusMonths(3);
                                                        break;

                                                case HALF_YEARLY:
                                                        interestStart = interestStart.plusMonths(6);
                                                        break;

                                                case YEARLY:
                                                        interestStart = interestStart.plusMonths(12);
                                                        break;
                                        }

                                        if (LocalDate.now().isAfter(interestStart)) {

                                                long daysLate = ChronoUnit.DAYS.between(bill.getCreatedDate(),
                                                                LocalDate.now());

                                                interest = Math.round(
                                                                bill.getMaintenanceAmount()
                                                                                * policy.getInterestRate()
                                                                                * daysLate
                                                                                / (365.0 * 100));
                                        }
                                }

                        }

                        bill.setInterestAmount(interest);

                        double total = (bill.getMaintenanceAmount() != null
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

        public List<Receipt> getBillingReceipts(BillingReceiptRequest request) {

                List<Billing> bills = billingRepository.findByFlatIdInAndSocietyIdAndFinancialYearId(
                                request.getFlatIds(),
                                request.getSocietyId(),
                                request.getFinancialYearId());

                List<Long> receiptIds = bills.stream()
                                .map(Billing::getReceiptId)
                                .filter(Objects::nonNull)
                                .distinct()
                                .toList();

                return receiptRepository.findAllById(receiptIds);
        }

        public InterestCalculationResponse calculateInterest(
                        InterestCalculationRequest request) {

                List<Billing> bills = billingRepository.findAllById(request.getBillIds());

                if (bills.isEmpty()) {
                        return new InterestCalculationResponse(0.0, 0.0, 0.0, 0.0);
                }

                Long societyId = bills.get(0).getSociety().getId();
                Long financialYearId = bills.get(0).getFinancialYearId();

                SocietyBillingPolicy policy = societyBillingPolicyRepository
                                .findBySociety_IdAndFinancialYearId(societyId, financialYearId)
                                .orElse(null);

                double maintenanceAmount = 0.0;
                double interestAmount = 0.0;
                double discountAmount = 0.0;
                double totalAmount = 0.0;

                LocalDate paymentDate = request.getPaymentDate();

                for (Billing bill : bills) {

                        if (bill.getStatus() == PaymentStatus.PAID) {
                                continue;
                        }

                        double maintenance = bill.getMaintenanceAmount() == null
                                        ? 0.0
                                        : bill.getMaintenanceAmount();

                        double interest = 0.0;
                        double discount = bill.getDiscountAmount() == null
                                        ? 0.0
                                        : bill.getDiscountAmount();

                        if (policy != null && bill.getDueDate() != null) {

                                LocalDate interestStart = bill.getCreatedDate();

                                switch (policy.getInterestType()) {
                                        case MONTHLY:
                                                interestStart = interestStart.plusMonths(1);
                                                break;
                                        case QUARTERLY:
                                                interestStart = interestStart.plusMonths(3);
                                                break;
                                        case HALF_YEARLY:
                                                interestStart = interestStart.plusMonths(6);
                                                break;
                                        case YEARLY:
                                                interestStart = interestStart.plusMonths(12);
                                                break;
                                }

                                if (paymentDate.isAfter(interestStart)) {

                                        long daysLate = ChronoUnit.DAYS.between(
                                                        bill.getCreatedDate(),
                                                        paymentDate);

                                        daysLate = Math.max(0, daysLate);

                                        // Annual interest rate converted to daily interest
                                        interest = maintenance
                                                        * policy.getInterestRate()
                                                        * daysLate
                                                        / (365.0 * 100.0);
                                }

                        }

                        maintenanceAmount += maintenance;
                        interestAmount += interest;
                        discountAmount += discount;

                        totalAmount += maintenance + interest - discount;
                }

                interestAmount = Math.round(interestAmount);
                totalAmount = Math.round(totalAmount);
                maintenanceAmount = Math.round(maintenanceAmount);
                System.out.println("TotalAmount:" + totalAmount);

                return new InterestCalculationResponse(
                                maintenanceAmount,
                                interestAmount,
                                discountAmount,
                                totalAmount);
        }

        public InterestCalculationResponse calculateDiscount(
                        InterestCalculationRequest request) {

                System.out.println("Count:" + request.getSelectedCount());
                System.out.println("Date:" + request.getPaymentDate());

                List<Billing> bills = billingRepository.findAllById(request.getBillIds());

                if (bills.isEmpty()) {
                        return new InterestCalculationResponse(0.0, 0.0, 0.0, 0.0);
                }

                Long societyId = bills.get(0).getSociety().getId();

                List<DiscountPolicy> policies = discountPolicyRepository.findBySociety_IdAndActiveTrue(societyId);

                DiscountPolicy policy = policies.isEmpty() ? null : policies.get(0);

                double maintenanceAmount = 0.0;
                double interestAmount = 0.0;

                for (Billing bill : bills) {
                        maintenanceAmount += bill.getMaintenanceAmount() == null
                                        ? 0.0
                                        : bill.getMaintenanceAmount();
                        interestAmount += bill.getInterestAmount() == null ? 0.0 : bill.getInterestAmount();
                }

                double discountAmount = 0.0;

                System.out.println("Discount:" + discountAmount);

                if (policy != null && request.getSelectedCount() != null
                                && request.getSelectedCount() == 12 && request.getPaymentDate() != null
                                && !request.getPaymentDate().isAfter(policy.getPaidBeforeDate())) {

                        discountAmount = BigDecimal.valueOf(maintenanceAmount)
                                        .multiply(policy.getDiscountPercent())
                                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                                        .doubleValue();
                }
                Double totalAmount = maintenanceAmount + interestAmount - discountAmount;
                return new InterestCalculationResponse(
                                maintenanceAmount,
                                interestAmount,
                                discountAmount,
                                totalAmount);
        }

        public void generateFinancialYearBills(BillGenerateRequest request) {

                List<String> months = List.of(
                                "APRIL",
                                "MAY",
                                "JUNE",
                                "JULY",
                                "AUGUST",
                                "SEPTEMBER",
                                "OCTOBER",
                                "NOVEMBER",
                                "DECEMBER",
                                "JANUARY",
                                "FEBRUARY",
                                "MARCH");

                int startYear = request.getYear();

                for (String month : months) {

                        int billYear = List.of(
                                        "JANUARY",
                                        "FEBRUARY",
                                        "MARCH").contains(month)
                                                        ? startYear + 1
                                                        : startYear;

                        BillGenerateRequest req = new BillGenerateRequest();

                        req.setMonth(month);
                        req.setYear(billYear);
                        req.setSocietyId(request.getSocietyId());
                        req.setFinancialYearId(request.getFinancialYearId());
                        req.setCreatedBy(request.getCreatedBy());
                        req.setGlReceivable(request.getGlReceivable());
                        req.setGlCreditAccount(request.getGlCreditAccount());

                        generateMonthlyBills(
                                        req.getSocietyId(),
                                        req.getMonth(),
                                        req.getYear(),
                                        req.getCreatedBy(),
                                        req.getFinancialYearId(),
                                        req.getGlReceivable(),
                                        req.getGlCreditAccount());
                }
        }

}
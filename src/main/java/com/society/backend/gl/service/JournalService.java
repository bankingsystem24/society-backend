package com.society.backend.gl.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.society.backend.entity.AccountingYear;
import com.society.backend.entity.Flat;
import com.society.backend.entity.Member;
import com.society.backend.gl.dto.JournalViewDTO;
import com.society.backend.gl.entity.*;
import com.society.backend.gl.repository.*;
import com.society.backend.repository.AccountingYearRepository;
import com.society.backend.repository.FlatRepository;
import com.society.backend.repository.MemberRepository;

@Service
@Transactional
public class JournalService {

        private final JournalEntryRepository journalRepo;
        private final JournalEntryLineRepository lineRepo;
        private final JournalViewRepository journalViewRepository;
        private final LedgerBalanceService ledgerBalanceService;
        private final AccountingYearRepository accountingYearRepository;
        private final MemberRepository memberRepository;
        private final FlatRepository flatRepository;

        public JournalService(
                        JournalEntryRepository journalRepo,
                        JournalEntryLineRepository lineRepo,
                        JournalViewRepository journalViewRepository,
                        LedgerBalanceService ledgerBalanceService,
                        AccountingYearRepository accountingYearRepository,
                        MemberRepository memberRepository,
                        FlatRepository flatRepository) {
                this.journalRepo = journalRepo;
                this.lineRepo = lineRepo;
                this.journalViewRepository = journalViewRepository;
                this.ledgerBalanceService = ledgerBalanceService;
                this.accountingYearRepository = accountingYearRepository;
                this.memberRepository = memberRepository;
                this.flatRepository = flatRepository;
        }

        // =====================================================
        // CORE JOURNAL ENTRY
        // =====================================================

        public Long createJournalEntry(
                        String voucherNo,
                        String voucherType,
                        String narration,
                        String referenceType,
                        Long referenceId,
                        Double totalAmount,
                        Long societyId,
                        Integer debitGlCode,
                        Double debitAmount,

                        Integer creditGlCode,
                        Double creditAmount,

                        String entityType,
                        Long entityId,
                        Long financialYearId,
                        Long createdBy,
                        Long flatId,
                        Member member) {

                JournalEntry entry = new JournalEntry();
                entry.setVoucherNo(voucherNo);
                entry.setEntryDate(LocalDate.now());
                entry.setVoucherType(VoucherType.valueOf(voucherType));
                entry.setNarration(narration);
                entry.setReferenceType(referenceType);
                entry.setReferenceId(referenceId);
                entry.setTotalAmount(totalAmount);
                entry.setStatus("POSTED");
                entry.setSocietyId(societyId);
                entry.setCreatedAt(LocalDateTime.now());
                entry.setCreatedBy(createdBy);
                entry.setFinancialYearId(financialYearId);

                JournalEntry savedEntry = journalRepo.save(entry);

                // DEBIT LINE
                JournalEntryLine debitLine = new JournalEntryLine();
                debitLine.setJournalEntry(savedEntry);
                debitLine.setLineNo(1);
                debitLine.setGlCode(debitGlCode);
                debitLine.setDebitAmount(debitAmount != null ? debitAmount : 0.0);
                debitLine.setCreditAmount(0.0);
                debitLine.setEntityType(entityType);
                debitLine.setEntityId(entityId);
                debitLine.setSocietyId(societyId);
                debitLine.setRemarks("Debit Entry");
                debitLine.setFlatId(flatId);
                debitLine.setMember(member);
                debitLine.setFinancialYearId(financialYearId);
                lineRepo.save(debitLine);

                // CREDIT LINE
                JournalEntryLine creditLine = new JournalEntryLine();
                creditLine.setJournalEntry(savedEntry);
                creditLine.setLineNo(2);
                creditLine.setGlCode(creditGlCode);
                creditLine.setDebitAmount(0.0);
                creditLine.setCreditAmount(creditAmount != null ? creditAmount : 0.0);
                creditLine.setEntityType(entityType);
                creditLine.setEntityId(entityId);
                creditLine.setSocietyId(societyId);
                creditLine.setRemarks("Credit Entry");
                creditLine.setFlatId(flatId);
                creditLine.setMember(member);
                creditLine.setFinancialYearId(financialYearId);
                lineRepo.save(creditLine);

                return savedEntry.getId();
        }

        // =====================================================
        // BILL ENTRY
        // =====================================================

        public Long createMaintenanceBillEntry(
                        Long billId,
                        Member member,
                        Double amount,
                        Long societyId,
                        Long createdBy,
                        Long flatId,
                        Long financialYearId,
                        Integer glReceivable,
                        Integer glCreditAccount) {

                final Integer RECEIVABLE_GL = glReceivable;  // 1101;
                final Integer INCOME_GL = glCreditAccount; // 3000; // ✅ FIXED (NOT 4001)

                Long journalId = createJournalEntry(
                                "BILL-" + billId,
                                "BILL",
                                "Maintenance Bill Generated",
                                "BILL",
                                billId,
                                amount,
                                societyId,
                                RECEIVABLE_GL,
                                amount,
                                INCOME_GL,
                                amount,
                                "MEMBER",
                                member != null ? member.getId() : null,
                                financialYearId,
                                createdBy,
                                flatId,
                                member);

                // ================= LEDGER UPDATE =================

                ledgerBalanceService.updateBalance(
                                societyId,
                                RECEIVABLE_GL,
                                member != null ? member.getId() : null,
                                "MEMBER",
                                amount,
                                0.0);

                ledgerBalanceService.updateBalance(
                                societyId,
                                INCOME_GL,
                                null,
                                "SOCIETY",
                                0.0,
                                amount);

                return journalId;
        }

        // =====================================================
        // RECEIPT ENTRY
        // =====================================================

        @Transactional
        public Long createReceiptEntry(
                        Long receiptId,
                        Long memberId,
                        Double maintenanceAmount,
                        Double interestAmount,
                        Double discountAmount,
                        Double totalAmount,
                        String paymentMode,
                        Long societyId,
                        Long createdBy,
                        Long flatId,
                        Long financialYearId) {

                Integer receiptGl;

                // ================= PAYMENT MODE TO GL =================
                switch (paymentMode.toUpperCase()) {
                        case "CASH":
                                receiptGl = 1000;
                                break;

                        case "UPI":
                        case "NEFT":
                        case "RTGS":
                        case "IMPS":
                        case "CHEQUE":
                        case "CARD":
                        case "NETBANKING":
                        case "ONLINE":
                                receiptGl = 1001;
                                break;

                        default:
                                throw new RuntimeException("Unsupported Payment Mode: " + paymentMode);
                }

                Integer DISCOUNT_GL = 5100;
                Integer INTEREST_GL = 3020;

                // ================= HEADER ENTRY =================
                JournalEntry entry = new JournalEntry();
                entry.setVoucherNo("RCPT-" + receiptId);
                entry.setEntryDate(LocalDate.now());
                entry.setVoucherType(VoucherType.RECEIPT);
                entry.setNarration("Payment Receipt");
                entry.setReferenceType("RECEIPT");
                entry.setReferenceId(receiptId);
                entry.setTotalAmount(totalAmount);
                entry.setStatus("POSTED");
                entry.setSocietyId(societyId);
                entry.setCreatedAt(LocalDateTime.now());
                entry.setCreatedBy(createdBy);
                entry.setFinancialYearId(financialYearId);
                JournalEntry savedEntry = journalRepo.save(entry);

                int lineNo = 1;

                // ================= DR CASH / BANK =================
                createLine(
                                savedEntry,
                                lineNo++,
                                receiptGl,
                                totalAmount,
                                0.0,
                                "RECEIPT",
                                receiptId,
                                societyId,
                                flatId,
                                financialYearId);

                // ================= CR MAINTENANCE =================
                if (maintenanceAmount != null && maintenanceAmount > 0) {
                        createLine(
                                        savedEntry,
                                        lineNo++,
                                        1101,
                                        0.0,
                                        maintenanceAmount,
                                        "MEMBER RECEIVABLE",
                                        memberId,
                                        societyId,
                                        flatId,
                                        financialYearId);
                }

                if (totalAmount != null && totalAmount > 0 && maintenanceAmount == 0) {
                        createLine(
                                        savedEntry,
                                        lineNo++,
                                        1101,
                                        0.0,
                                        totalAmount,
                                        "MEMBER RECEIVABLE",
                                        memberId,
                                        societyId,
                                        flatId,
                                        financialYearId);
                }

                if (interestAmount != null && interestAmount > 0) {
                        createLine(
                                        savedEntry,
                                        lineNo++,
                                        INTEREST_GL,
                                        0.0,
                                        interestAmount,
                                        "INTEREST",
                                        memberId,
                                        societyId,
                                        flatId,
                                        financialYearId);
                }

                // ================= DR DISCOUNT =================
                if (discountAmount != null && discountAmount > 0) {
                        createLine(
                                        savedEntry,
                                        lineNo++,
                                        DISCOUNT_GL,
                                        discountAmount,
                                        0.0,
                                        "DISCOUNT",
                                        memberId,
                                        societyId,
                                        flatId,
                                        financialYearId);
                }

                return savedEntry.getId();
        }

        // =====================================================
        // GENERAL JOURNAL
        // =====================================================

        public Long createGeneralJournal(
                        String voucherNo,
                        String narration,
                        Integer debitGlCode,
                        Integer creditGlCode,
                        Double amount,
                        Long societyId,
                        Long createdBy,
                        Long flatId,
                        Long memberId,
                        Long financialYearId) {

                Member member = memberRepository.findById(memberId)
                                .orElseThrow(() -> new RuntimeException("Member not found"));

                return createJournalEntry(
                                voucherNo,
                                "JOURNAL",
                                narration,
                                "JOURNAL",
                                null,
                                amount,
                                societyId,
                                debitGlCode,
                                amount,
                                creditGlCode,
                                amount,
                                "SOCIETY",
                                financialYearId,
                                societyId,
                                createdBy,
                                null,
                                member);
        }

        // =====================================================
        // VIEW JOURNAL
        // =====================================================

        public List<JournalViewDTO> getJournal(Long societyId) {
                AccountingYear fy = accountingYearRepository
                                .findBySocietyIdAndIsActiveTrue(societyId)
                                .orElseThrow(() -> new RuntimeException("Active FY not found"));

                return journalViewRepository.getJournalView(
                                societyId,
                                fy.getStartDate(),
                                fy.getEndDate());
        }

        private void createLine(
                        JournalEntry journalEntry,
                        int lineNo,
                        Integer glCode,
                        Double debit,
                        Double credit,
                        String entityType,
                        Long entityId,
                        Long societyId,
                        Long flatId,
                        Long financialYearId) {
                JournalEntryLine line = new JournalEntryLine();

                line.setJournalEntry(journalEntry);
                line.setLineNo(lineNo);
                line.setGlCode(glCode);
                line.setDebitAmount(debit != null ? debit : 0.0);
                line.setCreditAmount(credit != null ? credit : 0.0);
                line.setEntityType(entityType);
                line.setEntityId(entityId);
                line.setSocietyId(societyId);
                line.setFlatId(flatId);
                line.setRemarks("AUTO");
                line.setFinancialYearId(financialYearId);

                Flat flat = flatRepository.findById(flatId)
                                .orElseThrow(() -> new RuntimeException("Flat not found"));

                Member member = flat.getOwner();

                line.setMember(member);

                lineRepo.save(line);
        }

        // =====================================================
        // SINKING FUND ENTRY
        // =====================================================

        public Long createSinkingFundEntry(
                        Long sinkingFundId,
                        Member member,
                        Double amount,
                        Long societyId,
                        Long createdBy,
                        Long flatId,
                        Long financialYearId) {

                final Integer RECEIVABLE_GL = 1101;
                final Integer SINKING_FUND_GL = 5010;

                Long journalId = createJournalEntry(
                                "SF-" + sinkingFundId,
                                "BILL",
                                "Sinking Fund Generated",
                                "SINKING_FUND",
                                sinkingFundId,
                                amount,
                                societyId,
                                RECEIVABLE_GL,
                                amount,
                                SINKING_FUND_GL,
                                amount,
                                "MEMBER",
                                member != null ? member.getId() : null,
                                financialYearId,
                                createdBy,
                                flatId,
                                member);

                // ================= LEDGER UPDATE =================

                ledgerBalanceService.updateBalance(
                                societyId,
                                RECEIVABLE_GL,
                                member != null ? member.getId() : null,
                                "MEMBER",
                                amount,
                                0.0);

                ledgerBalanceService.updateBalance(
                                societyId,
                                SINKING_FUND_GL,
                                null,
                                "SOCIETY",
                                0.0,
                                amount);

                return journalId;
        }

        public Long createContributionEntry(
                        Long contributionId,
                        Member member,
                        Double amount,
                        Long societyId,
                        Long createdBy,
                        Long flatId,
                        Long financialYearId) {

                final Integer RECEIVABLE_GL = 1101;
                final Integer CONTRIBUTION_INCOME_GL = 3002;

                Long journalId = createJournalEntry(
                                "CONTRIB-" + contributionId,
                                "BILL",
                                "Contribution Generated",
                                "CONTRIBUTION",
                                contributionId,
                                amount,
                                societyId,
                                RECEIVABLE_GL,
                                amount,
                                CONTRIBUTION_INCOME_GL,
                                amount,
                                "MEMBER",
                                member != null ? member.getId() : null,
                                financialYearId,
                                createdBy,
                                flatId,
                                member);

                // ================= LEDGER UPDATE =================

                ledgerBalanceService.updateBalance(
                                societyId,
                                RECEIVABLE_GL,
                                member != null ? member.getId() : null,
                                "MEMBER",
                                amount,
                                0.0);

                ledgerBalanceService.updateBalance(
                                societyId,
                                CONTRIBUTION_INCOME_GL,
                                null,
                                "SOCIETY",
                                0.0,
                                amount);

                return journalId;
        }

}
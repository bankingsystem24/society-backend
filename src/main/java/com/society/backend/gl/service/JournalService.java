package com.society.backend.gl.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.society.backend.entity.AccountingYear;
import com.society.backend.entity.Member;
import com.society.backend.gl.dto.JournalViewDTO;
import com.society.backend.gl.entity.*;
import com.society.backend.gl.repository.*;
import com.society.backend.repository.AccountingYearRepository;
import com.society.backend.repository.MemberRepository;

@Service
@Transactional
public class JournalService {

        @Autowired
        private JournalEntryRepository journalRepo;

        @Autowired
        private JournalEntryLineRepository lineRepo;

        @Autowired
        private JournalViewRepository journalViewRepository;

        @Autowired
        private LedgerBalanceService ledgerBalanceService;

        @Autowired
        private AccountingYearRepository accountingYearRepository;

        @Autowired
        private JournalEntryLineRepository journalEntryLineRepository;

        @Autowired
        private MemberRepository memberRepository;

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

                JournalEntry savedEntry = journalRepo.save(entry);

                // DEBIT LINE
                JournalEntryLine debitLine = new JournalEntryLine();
                debitLine.setJournalId(savedEntry.getId());
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
                lineRepo.save(debitLine);

                // CREDIT LINE
                JournalEntryLine creditLine = new JournalEntryLine();
                creditLine.setJournalId(savedEntry.getId());
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
                        Long flatId) {

                Long journalId = createJournalEntry(
                                "BILL-" + billId,
                                "BILL",
                                "Maintenance Bill Generated",
                                "BILL",
                                billId,
                                amount,
                                societyId,
                                1101, // DR Member Receivable
                                amount,
                                4001, // CR Income
                                amount,
                                "SOCIETY",
                                societyId,
                                createdBy,
                                flatId,
                                member);

                // LEDGER UPDATE (RECEIVABLE INCREASE)
                ledgerBalanceService.updateBalance(
                                societyId,
                                1101,
                                null,
                                "SOCIETY",
                                amount,
                                0.0);

                // LEDGER UPDATE (INCOME)
                ledgerBalanceService.updateBalance(
                                societyId,
                                4001,
                                null,
                                "SOCIETY",
                                0.0,
                                amount);

                return journalId;
        }

        // =====================================================
        // RECEIPT ENTRY
        // =====================================================

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
                        Long flatId) {

                Integer BANK_GL = "CASH".equalsIgnoreCase(paymentMode) ? 1001 : 1002;

                // IMPORTANT CHANGE: Receivable instead of Maintenance Income
                Integer RECEIVABLE_GL = 1101;

                Integer INTEREST_GL = 4002;
                Integer DISCOUNT_GL = 5001;

                // ================= HEADER =================
                JournalEntry entry = new JournalEntry();
                entry.setVoucherNo("RCPT-" + receiptId);
                entry.setEntryDate(LocalDate.now());
                entry.setVoucherType(VoucherType.RECEIPT);
                entry.setNarration("Maintenance + Interest Collection");
                entry.setReferenceType("RECEIPT");
                entry.setReferenceId(receiptId);
                entry.setTotalAmount(totalAmount);
                entry.setStatus("POSTED");
                entry.setSocietyId(societyId);
                entry.setCreatedAt(LocalDateTime.now());
                entry.setCreatedBy(createdBy);

                JournalEntry savedEntry = journalRepo.save(entry);

                Long journalId = savedEntry.getId();
                int lineNo = 1;

                // ================= BANK (DR) =================
                createLine(
                                journalId,
                                lineNo++,
                                BANK_GL,
                                totalAmount,
                                0.0,
                                "BANK",
                                null,
                                societyId,
                                flatId);

                // ================= RECEIVABLE (CR) =================
                // This replaces Maintenance Income reversal
                if (maintenanceAmount != null && maintenanceAmount > 0) {
                        createLine(
                                        journalId,
                                        lineNo++,
                                        RECEIVABLE_GL,
                                        0.0,
                                        maintenanceAmount,
                                        "SOCIETY",
                                        memberId,
                                        societyId,
                                        flatId);
                }

                // ================= INTEREST (CR) =================
                if (interestAmount != null && interestAmount > 0) {
                        createLine(
                                        journalId,
                                        lineNo++,
                                        INTEREST_GL,
                                        0.0,
                                        interestAmount,
                                        "INCOME",
                                        memberId,
                                        societyId,
                                        flatId);
                }

                // ================= DISCOUNT (DR) =================
                if (discountAmount != null && discountAmount > 0) {
                        createLine(
                                        journalId,
                                        lineNo++,
                                        DISCOUNT_GL,
                                        discountAmount,
                                        0.0,
                                        "EXPENSE",
                                        memberId,
                                        societyId,
                                        flatId);
                }

                // ================= LEDGER UPDATES =================
                ledgerBalanceService.updateBalance(
                                societyId,
                                BANK_GL,
                                null,
                                "BANK",
                                totalAmount,
                                0.0);

                if (maintenanceAmount != null && maintenanceAmount > 0) {
                        ledgerBalanceService.updateBalance(
                                        societyId,
                                        RECEIVABLE_GL,
                                        null,
                                        "SOCIETY",
                                        0.0,
                                        maintenanceAmount);
                }

                if (interestAmount != null && interestAmount > 0) {
                        ledgerBalanceService.updateBalance(
                                        societyId,
                                        INTEREST_GL,
                                        null,
                                        "INCOME",
                                        0.0,
                                        interestAmount);
                }

                if (discountAmount != null && discountAmount > 0) {
                        ledgerBalanceService.updateBalance(
                                        societyId,
                                        DISCOUNT_GL,
                                        null,
                                        "EXPENSE",
                                        discountAmount,
                                        0.0);
                }

                return journalId;
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
                        Long memberId) {

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
                        Long journalId,
                        int lineNo,
                        Integer glCode,
                        Double debit,
                        Double credit,
                        String entityType,
                        Long entityId,
                        Long societyId,
                        Long flatId) {
                JournalEntryLine line = new JournalEntryLine();

                line.setJournalId(journalId);
                line.setLineNo(lineNo);
                line.setGlCode(glCode);
                line.setDebitAmount(debit != null ? debit : 0.0);
                line.setCreditAmount(credit != null ? credit : 0.0);
                line.setEntityType(entityType);
                line.setEntityId(entityId);
                line.setSocietyId(societyId);
                line.setFlatId(flatId);
                line.setRemarks("AUTO");

                journalEntryLineRepository.save(line);
        }
}
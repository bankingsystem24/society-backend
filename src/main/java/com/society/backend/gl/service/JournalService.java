package com.society.backend.gl.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.society.backend.entity.AccountingYear;
import com.society.backend.gl.dto.JournalViewDTO;
import com.society.backend.gl.entity.*;
import com.society.backend.gl.repository.*;
import com.society.backend.repository.AccountingYearRepository;

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
        private JournalEntryRepository journalEntryRepository;

        @Autowired
        private JournalEntryLineRepository journalEntryLineRepository;

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
                        Long entityId) {

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
                lineRepo.save(creditLine);

                return savedEntry.getId();
        }

        // =====================================================
        // BILL ENTRY
        // =====================================================

        public Long createMaintenanceBillEntry(
                        Long billId,
                        Long memberId,
                        Double amount,
                        Long societyId) {

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
                                societyId);

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
                        Long societyId) {

                Integer BANK_GL = "CASH".equalsIgnoreCase(paymentMode) ? 1001 : 1002;
                Integer MAINTENANCE_GL = 4001;
                Integer INTEREST_GL = 4002;
                Integer DISCOUNT_GL = 5001;

                // ================= HEADER ONLY =================
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

                JournalEntry savedEntry = journalRepo.save(entry);

                Long journalId = savedEntry.getId();

                int lineNo = 1;

                // ================= BANK (DR) =================
                createLine(journalId, lineNo++, BANK_GL, totalAmount, 0.0, "BANK", null, societyId);

                // ================= MAINTENANCE (CR) =================
                createLine(journalId, lineNo++, MAINTENANCE_GL, 0.0, maintenanceAmount, "INCOME", memberId, societyId);

                // ================= INTEREST (CR) =================
                createLine(journalId, lineNo++, INTEREST_GL, 0.0, interestAmount, "INCOME", memberId, societyId);

                // ================= DISCOUNT (DR) =================
                createLine(journalId, lineNo++, DISCOUNT_GL, discountAmount, 0.0, "EXPENSE", memberId, societyId);

                // ================= LEDGER =================
                ledgerBalanceService.updateBalance(societyId, BANK_GL, null, "BANK", totalAmount, 0.0);

                ledgerBalanceService.updateBalance(societyId, MAINTENANCE_GL, null, "INCOME", 0.0, maintenanceAmount);

                ledgerBalanceService.updateBalance(societyId, INTEREST_GL, null, "INCOME", 0.0, interestAmount);

                ledgerBalanceService.updateBalance(societyId, DISCOUNT_GL, null, "EXPENSE", discountAmount, 0.0);

                return journalId;
        } // =====================================================
          // EXPENSE ENTRY
          // =====================================================

        public Long createExpenseEntry(
                        String voucherNo,
                        String narration,
                        Integer expenseGlCode,
                        Double amount,
                        Long vendorId,
                        Long societyId) {

                Long journalId = createJournalEntry(
                                voucherNo,
                                "PAYMENT",
                                narration,
                                "EXPENSE",
                                vendorId,
                                amount,
                                societyId,

                                expenseGlCode,
                                amount,

                                1002,
                                amount,

                                "SOCIETY",
                                societyId);

                // EXPENSE INCREASE
                ledgerBalanceService.updateBalance(
                                societyId,
                                expenseGlCode,
                                null,
                                "SOCIETY",
                                amount,
                                0.0);

                // BANK DECREASE
                ledgerBalanceService.updateBalance(
                                societyId,
                                1002,
                                null,
                                "SOCIETY",
                                0.0,
                                amount);

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
                        Long societyId) {

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
                                societyId);
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
                        Long societyId) {
                JournalEntryLine line = new JournalEntryLine();

                line.setJournalId(journalId);
                line.setLineNo(lineNo);
                line.setGlCode(glCode);
                line.setDebitAmount(debit != null ? debit : 0.0);
                line.setCreditAmount(credit != null ? credit : 0.0);
                line.setEntityType(entityType);
                line.setEntityId(entityId);
                line.setSocietyId(societyId);
                line.setRemarks("AUTO");

                journalEntryLineRepository.save(line);
        }
}
package com.society.backend.gl.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.society.backend.gl.dto.JournalViewDTO;
import com.society.backend.gl.entity.*;
import com.society.backend.gl.repository.*;

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
            Long entityId
    ) {

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
            Long societyId
    ) {

        Long journalId = createJournalEntry(
                "BILL-" + billId,
                "BILL",
                "Maintenance Bill Generated",
                "BILL",
                billId,
                amount,
                societyId,

                1101,  // DR Member Receivable
                amount,

                4001,  // CR Income
                amount,

                "SOCIETY",
                societyId
        );

        // LEDGER UPDATE (RECEIVABLE INCREASE)
        ledgerBalanceService.updateBalance(
                societyId,
                1101,
                null,
                "SOCIETY",
                amount,
                0.0
        );

        // LEDGER UPDATE (INCOME)
        ledgerBalanceService.updateBalance(
                societyId,
                4001,
                null,
                "SOCIETY",
                0.0,
                amount
        );

        return journalId;
    }

    // =====================================================
    // RECEIPT ENTRY
    // =====================================================

    public Long createReceiptEntry(
            Long receiptId,
            Long memberId,
            Double amount,
            String paymentMode,
            Long societyId
    ) {

        Integer bankGlCode =
                "CASH".equalsIgnoreCase(paymentMode) ? 1001 : 1002;

        Long journalId = createJournalEntry(
                "RCPT-" + receiptId,
                "RECEIPT",
                "Maintenance Payment Received",
                "RECEIPT",
                receiptId,
                amount,
                societyId,

                bankGlCode, // DR
                amount,

                1101, // CR
                amount,

                "SOCIETY",
                societyId
        );

        // BANK INCREASE
        ledgerBalanceService.updateBalance(
                societyId,
                bankGlCode,
                null,
                "SOCIETY",
                amount,
                0.0
        );

        // RECEIVABLE DECREASE
        ledgerBalanceService.updateBalance(
                societyId,
                1101,
                null,
                "SOCIETY",
                0.0,
                amount
        );

        return journalId;
    }

    // =====================================================
    // EXPENSE ENTRY
    // =====================================================

    public Long createExpenseEntry(
            String voucherNo,
            String narration,
            Integer expenseGlCode,
            Double amount,
            Long vendorId,
            Long societyId
    ) {

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
                societyId
        );

        // EXPENSE INCREASE
        ledgerBalanceService.updateBalance(
                societyId,
                expenseGlCode,
                null,
                "SOCIETY",
                amount,
                0.0
        );

        // BANK DECREASE
        ledgerBalanceService.updateBalance(
                societyId,
                1002,
                null,
                "SOCIETY",
                0.0,
                amount
        );

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
            Long societyId
    ) {

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
                societyId
        );
    }

    // =====================================================
    // VIEW JOURNAL
    // =====================================================

    public List<JournalViewDTO> getJournal(Long societyId) {
        return journalViewRepository.getJournalView(societyId);
    }
}
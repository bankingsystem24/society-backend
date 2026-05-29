package com.society.backend.gl.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.society.backend.gl.dto.JournalViewDTO;
import com.society.backend.gl.entity.JournalEntry;
import com.society.backend.gl.entity.JournalEntryLine;
import com.society.backend.gl.repository.JournalEntryLineRepository;
import com.society.backend.gl.repository.JournalEntryRepository;
import com.society.backend.gl.repository.JournalViewRepository;
import com.society.backend.gl.entity.VoucherType;

@Service
@Transactional
public class JournalService {

    @Autowired
    private JournalEntryRepository journalRepo;

    @Autowired
    private JournalEntryLineRepository lineRepo;

    @Autowired
    private JournalViewRepository journalViewRepository;

    // =====================================================
    // COMMON JOURNAL ENTRY METHOD
    // =====================================================

    public Long createJournalEntry(

            String voucherNo,
            String voucherType,

            String narration,

            String referenceType,
            Long referenceId,

            Double totalAmount,

            Long societyId,

            // DEBIT
            Integer debitGlCode,
            Double debitAmount,

            // CREDIT
            Integer creditGlCode,
            Double creditAmount,

            // ENTITY
            String entityType,
            Long entityId

    ) {

        // =================================================
        // HEADER
        // =================================================

        JournalEntry entry = new JournalEntry();

        entry.setVoucherNo(voucherNo);

        entry.setEntryDate(LocalDate.now());

        entry.setVoucherType(
            VoucherType.valueOf(voucherType)
        );

        entry.setNarration(narration);

        entry.setReferenceType(referenceType);

        entry.setReferenceId(referenceId);

        entry.setTotalAmount(totalAmount);

        entry.setStatus("POSTED");

        entry.setSocietyId(societyId);

        entry.setCreatedAt(LocalDateTime.now());

        JournalEntry savedEntry = journalRepo.save(entry);

        // =================================================
        // DEBIT LINE
        // =================================================

        JournalEntryLine debitLine = new JournalEntryLine();

        debitLine.setJournalId(savedEntry.getId());

        debitLine.setLineNo(1);

        debitLine.setGlCode(debitGlCode);

        debitLine.setDebitAmount(
                debitAmount != null ? debitAmount : 0.0
        );

        debitLine.setCreditAmount(0.0);

        debitLine.setEntityType(entityType);

        debitLine.setEntityId(entityId);

        debitLine.setRemarks("Debit Entry");

        debitLine.setSocietyId(societyId);

        lineRepo.save(debitLine);

        // =================================================
        // CREDIT LINE
        // =================================================

        JournalEntryLine creditLine = new JournalEntryLine();

        creditLine.setJournalId(savedEntry.getId());

        creditLine.setLineNo(2);

        creditLine.setGlCode(creditGlCode);

        creditLine.setDebitAmount(0.0);

        creditLine.setCreditAmount(
                creditAmount != null ? creditAmount : 0.0
        );

        creditLine.setEntityType(entityType);

        creditLine.setEntityId(entityId);

        creditLine.setRemarks("Credit Entry");

        creditLine.setSocietyId(societyId);

        lineRepo.save(creditLine);

        return savedEntry.getId();
    }

    // =====================================================
    // MAINTENANCE BILL ENTRY
    // =====================================================

    public Long createMaintenanceBillEntry(

            Long billId,

            Long memberId,

            Double amount,

            Long societyId

    ) {

        return createJournalEntry(

                "BILL-" + billId,

                "BILL",

                "Maintenance Bill Generated",

                "BILL",

                billId,

                amount,

                societyId,

                // DR
                1101,
                amount,

                // CR
                4001,
                amount,

                "MEMBER",

                memberId
        );
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

        Integer bankGlCode = 1002;

        // CASH ACCOUNT
        if ("CASH".equalsIgnoreCase(paymentMode)) {
            bankGlCode = 1001;
        }

        return createJournalEntry(

                "RCPT-" + receiptId,

                "RECEIPT",

                "Maintenance Payment Received",

                "RECEIPT",

                receiptId,

                amount,

                societyId,

                // DR BANK/CASH
                bankGlCode,
                amount,

                // CR MEMBER RECEIVABLE
                1101,
                amount,

                "MEMBER",

                memberId
        );
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

        return createJournalEntry(

                voucherNo,

                "PAYMENT",

                narration,

                "EXPENSE",

                vendorId,

                amount,

                societyId,

                // DR EXPENSE
                expenseGlCode,
                amount,

                // CR BANK
                1002,
                amount,

                "VENDOR",

                vendorId
        );
    }

    // =====================================================
    // GENERAL JOURNAL ENTRY
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

                null,
                null
        );
    }

    public List<JournalViewDTO> getJournal(Long societyId) {

    return journalViewRepository.getJournalView(societyId);
}

}
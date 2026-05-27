package com.society.backend.gl.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.society.backend.gl.entity.JournalEntry;
import com.society.backend.gl.entity.JournalEntryLine;
import com.society.backend.gl.repository.JournalEntryRepository;
import com.society.backend.gl.repository.JournalEntryLineRepository;

@Service
public class JournalService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private JournalEntryLineRepository journalEntryLineRepository;

    // ================= BILL GENERATION JOURNAL =================
    @Transactional
    public void createBillJournal(Long billId,
                                  Long flatId,
                                  Long memberId,
                                  Double amount,
                                  Long societyId) {

        // 1. Header
        JournalEntry journal = new JournalEntry();
        journal.setEntryDate(LocalDate.now());
        journal.setNarration("Bill Generated");
        journal.setReferenceId(billId);
        journal.setReferenceType("BILL");
        journal.setSocietyId(societyId);

        journal = journalEntryRepository.save(journal);

        // 2. Debit Entry (Receivable)
        JournalEntryLine debit = new JournalEntryLine();
        debit.setJournalId(journal.getId());
        debit.setGlCode(1100); // Maintenance Receivable
        debit.setDebitAmount(amount);
        debit.setCreditAmount(0.0);
        debit.setFlatId(flatId);
        debit.setMemberId(memberId);
        debit.setSocietyId(societyId);

        // 3. Credit Entry (Income)
        JournalEntryLine credit = new JournalEntryLine();
        credit.setJournalId(journal.getId());
        credit.setGlCode(3000); // Maintenance Income
        credit.setDebitAmount(0.0);
        credit.setCreditAmount(amount);
        credit.setFlatId(flatId);
        credit.setMemberId(memberId);
        credit.setSocietyId(societyId);

        journalEntryLineRepository.saveAll(List.of(debit, credit));
    }

    // ================= PAYMENT JOURNAL =================
    @Transactional
    public void createPaymentJournal(Long receiptId,
                                     Long flatId,
                                     Long memberId,
                                     Double amount,
                                     String paymentMode,
                                     Long societyId
                                     ) {

        // 1. Header
        JournalEntry journal = new JournalEntry();
        journal.setEntryDate(LocalDate.now());
        journal.setNarration("Payment Received via " + paymentMode);
        journal.setReferenceId(receiptId);
        journal.setReferenceType("PAYMENT");
        journal.setSocietyId(societyId);

        journal = journalEntryRepository.save(journal);

        // 2. Debit (Bank/Cash)
        int cashOrBankGl = paymentMode != null && paymentMode.equalsIgnoreCase("CASH")
                ? 1000
                : 1010;

        JournalEntryLine debit = new JournalEntryLine();
        debit.setJournalId(journal.getId());
        debit.setGlCode(cashOrBankGl);
        debit.setDebitAmount(amount);
        debit.setCreditAmount(0.0);
        debit.setFlatId(flatId);
        debit.setMemberId(memberId);
        debit.setSocietyId(societyId);

        // 3. Credit (Receivable)
        JournalEntryLine credit = new JournalEntryLine();
        credit.setJournalId(journal.getId());
        credit.setGlCode(1100);
        credit.setDebitAmount(0.0);
        credit.setCreditAmount(amount);
        credit.setFlatId(flatId);
        credit.setMemberId(memberId);
        credit.setSocietyId(societyId); 

        journalEntryLineRepository.saveAll(List.of(debit, credit));
    }
}
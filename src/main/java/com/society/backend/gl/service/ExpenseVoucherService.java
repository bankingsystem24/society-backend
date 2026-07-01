package com.society.backend.gl.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import com.society.backend.entity.Society;
import com.society.backend.gl.dto.ExpenseVoucherRequest;
import com.society.backend.gl.entity.ExpenseVoucher;
import com.society.backend.gl.repository.ExpenseVoucherRepository;
import com.society.backend.gl.repository.JournalEntryLineRepository;
import com.society.backend.gl.repository.JournalEntryRepository;
import com.society.backend.repository.SocietyRepository;

import jakarta.transaction.Transactional;

@Service
public class ExpenseVoucherService {

        private final JournalEntryLineRepository journalEntryLineRepository;
        private final ExpenseVoucherRepository expenseVoucherRepository;
        private final SocietyRepository societyRepository;
        private final JournalService journalService;
        private final JournalEntryRepository journalEntryRepository;

        public ExpenseVoucherService(JournalEntryLineRepository journalEntryLineRepository,
                        JournalEntryRepository journalEntryRepository,
                        ExpenseVoucherRepository expenseVoucherRepository,
                        SocietyRepository societyRepository,
                        JournalService journalService) {
                this.journalEntryLineRepository = journalEntryLineRepository;
                this.journalEntryRepository = journalEntryRepository;
                this.expenseVoucherRepository = expenseVoucherRepository;
                this.societyRepository = societyRepository;
                this.journalService = journalService;
        }

        // ================= SAVE =================

        public ExpenseVoucher save(ExpenseVoucherRequest request) {
                ExpenseVoucher voucher = new ExpenseVoucher();
                Society society = societyRepository
                                .findById(request.getSocietyId())
                                .orElseThrow(() -> new RuntimeException("Society not found"));
                voucher.setVoucherNo(generateVoucherNo());
                voucher.setVoucherDate(
                                LocalDate.parse(request.getVoucherDate()));
                voucher.setExpenseGlCode(
                                request.getExpenseGlCode());
                voucher.setAmount(
                                request.getAmount());
                voucher.setPaymentMode(
                                request.getPaymentMode());
                voucher.setNarration(
                                request.getNarration());
                voucher.setVendorId(
                                request.getVendorId());
                voucher.setSociety(society);
                voucher.setFinancialYearId(request.getFinancialYearId());

                ExpenseVoucher savedVoucher = expenseVoucherRepository.save(voucher);

                // Create Journal Entry here

                Long journalId = journalService.createJournalEntry(
                                savedVoucher.getVoucherNo(), // voucherNo
                                "EXPENSE", // voucherType
                                savedVoucher.getNarration(), // narration
                                "EXPENSE_VOUCHER", // referenceType
                                savedVoucher.getId(), // referenceId
                                savedVoucher.getAmount(), // totalAmount

                                request.getSocietyId(), // societyId

                                savedVoucher.getExpenseGlCode(), // Debit GL
                                savedVoucher.getAmount(), // Debit Amount

                                getPaymentGlCode(savedVoucher.getPaymentMode()), // Credit GL
                                savedVoucher.getAmount(), // Credit Amount

                                "VENDOR",
                                savedVoucher.getVendorId(),
                                savedVoucher.getFinancialYearId(),

                                0L, // createdBy
                                null, // flatId
                                null // member
                );

                savedVoucher.setJournalId(journalId);

                return expenseVoucherRepository.save(savedVoucher);
        }

        // ================= LIST =================

        public List<ExpenseVoucher> getBySocietyIdAndFinancialYearId(
                        Long societyId, Long financialYearId) {
                return expenseVoucherRepository
                                .findBySocietyIdAndFinancialYearIdOrderByVoucherDateDesc(
                                                societyId, financialYearId);
        }

        // ================= GET ONE =================

        public ExpenseVoucher getById(Long id) {

                return expenseVoucherRepository
                                .findById(id)
                                .orElseThrow(() -> new RuntimeException(
                                                "Expense voucher not found"));
        }

        // ================= DELETE =================
        @Transactional
        public void delete(Long id) {
                ExpenseVoucher voucher = getById(id);
                Long journalId = voucher.getJournalId();

                if (journalId != null) {
                        journalEntryRepository.deleteById(journalId);
                }
                expenseVoucherRepository.delete(voucher);
        }

        // ================= VOUCHER NUMBER =================

        private String generateVoucherNo() {

                long count = expenseVoucherRepository.count() + 1;

                return String.format(
                                "EXP%06d",
                                count);
        }

        private Integer getPaymentGlCode(String paymentMode) {

                if (paymentMode == null) { throw new RuntimeException("Payment mode cannot be null");}

                switch (paymentMode.toUpperCase()) {
                        case "CASH":
                                return 1000; // Cash Account GL
                        case "BANK":
                                return 1001; // Bank Account GL
                        case "UPI":
                                return 1001; // Bank Account GL
                        default:
                                throw new RuntimeException(
                                                "No GL mapping found for payment mode: " + paymentMode);
                }
        }

}
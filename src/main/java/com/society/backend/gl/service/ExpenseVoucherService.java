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

        private final ExpenseVoucherRepository expenseVoucherRepository;
        private final SocietyRepository societyRepository;
        private final JournalService journalService;
        private final JournalEntryRepository journalEntryRepository;
        private final JournalEntryLineRepository journalEntryLineRepository;

        public ExpenseVoucherService(JournalEntryRepository journalEntryRepository,
                        ExpenseVoucherRepository expenseVoucherRepository,
                        SocietyRepository societyRepository,
                        JournalService journalService,
                        JournalEntryLineRepository journalEntryLineRepository) {
                this.journalEntryRepository = journalEntryRepository;
                this.expenseVoucherRepository = expenseVoucherRepository;
                this.societyRepository = societyRepository;
                this.journalService = journalService;
                this.journalEntryLineRepository = journalEntryLineRepository;
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

                Integer paymentGlCode;

                if("CASH".equals(request.getPaymentMode())){
                        paymentGlCode = request.getGlCashInHand();
                } else {
                        paymentGlCode = request.getGlBankAccount();
                };

                Long journalId = journalService.createJournalEntry(
                                savedVoucher.getVoucherNo(), // voucherNo
                                "EXPENSES", // voucherType
                                savedVoucher.getNarration(), // narration
                                "EXPENSE_VOUCHER", // referenceType
                                savedVoucher.getId(), // referenceId
                                savedVoucher.getAmount(), // totalAmount
                                request.getSocietyId(), // societyId
                                savedVoucher.getExpenseGlCode(), // Debit GL
                                savedVoucher.getAmount(), // Debit Amount
                                paymentGlCode,
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

                if (journalId != null) {if (journalId != null) 
                {
                        journalEntryLineRepository.deleteByJournalId(journalId);

                }
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


}
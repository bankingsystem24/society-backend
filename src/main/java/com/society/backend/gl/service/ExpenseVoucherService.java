package com.society.backend.gl.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.society.backend.entity.Society;
import com.society.backend.gl.dto.ExpenseVoucherRequest;
import com.society.backend.gl.entity.ExpenseVoucher;
import com.society.backend.gl.repository.ExpenseVoucherRepository;
import com.society.backend.repository.SocietyRepository;

@Service
public class ExpenseVoucherService {

    private final ExpenseVoucherRepository expenseVoucherRepository;

    @Autowired
    private SocietyRepository societyRepository;

    @Autowired
    private JournalService journalService;

    public ExpenseVoucherService(
            ExpenseVoucherRepository expenseVoucherRepository) {

        this.expenseVoucherRepository = expenseVoucherRepository;
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

        System.out.println("Voucher"+voucher);
        
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

                0L, // createdBy
                null, // flatId
                null // member
        );

        savedVoucher.setJournalId(journalId);

        return expenseVoucherRepository.save(savedVoucher);
    }

    // ================= LIST =================

    public List<ExpenseVoucher> getBySociety(
            Long societyId) {

        return expenseVoucherRepository
                .findBySocietyIdOrderByVoucherDateDesc(
                        societyId);
    }

    // ================= GET ONE =================

    public ExpenseVoucher getById(Long id) {

        return expenseVoucherRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Expense voucher not found"));
    }

    // ================= DELETE =================

    public void delete(Long id) {

        ExpenseVoucher voucher = getById(id);

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

        if (paymentMode == null) {
            throw new RuntimeException("Payment mode cannot be null");
        }

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
package com.society.backend.gl.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.society.backend.entity.Society;
import com.society.backend.gl.dto.IncomeVoucherRequest;
import com.society.backend.gl.entity.IncomeVoucher;
import com.society.backend.gl.repository.IncomeVoucherRepository;
import com.society.backend.gl.repository.JournalEntryLineRepository;
import com.society.backend.gl.repository.JournalEntryRepository;
import com.society.backend.repository.SocietyRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class IncomeVoucherService {

    private final IncomeVoucherRepository incomeVoucherRepository;
    private final SocietyRepository societyRepository;
    private final JournalService journalService;
    private final JournalEntryRepository journalEntryRepository;
    private final JournalEntryLineRepository journalEntryLineRepository;

    public IncomeVoucherService(
            IncomeVoucherRepository incomeVoucherRepository,
            SocietyRepository societyRepository,
            JournalService journalService,
            JournalEntryRepository journalEntryRepository,
            JournalEntryLineRepository journalEntryLineRepository) {

        this.incomeVoucherRepository = incomeVoucherRepository;
        this.societyRepository = societyRepository;
        this.journalService = journalService;
        this.journalEntryRepository = journalEntryRepository;
        this.journalEntryLineRepository = journalEntryLineRepository;
    }

    public IncomeVoucher save(IncomeVoucherRequest request) {

        Society society = societyRepository.findById(request.getSocietyId())
                .orElseThrow(() -> new RuntimeException("Society not found"));

        IncomeVoucher voucher = new IncomeVoucher();

        voucher.setSociety(society);
        voucher.setVoucherDate(request.getVoucherDate());
        voucher.setIncomeGlCode(request.getIncomeGlCode());
        voucher.setAmount(request.getAmount());
        voucher.setPaymentMode(request.getPaymentMode());
        voucher.setNarration(request.getNarration());
        voucher.setReceivedFrom(request.getReceivedFrom());
        voucher.setFinancialYearId(request.getFinancialYearId());

        // Generate Voucher No
        voucher.setVoucherNo("INC-" + System.currentTimeMillis());

        System.out.println("GlCashInHand"+request.getGlCashInHand());
        
        // Create Journal Entry
        Long journalId = journalService.createJournalEntry(
                                voucher.getVoucherNo(), // voucherNo
                                "INCOME", // voucherType
                                voucher.getNarration(), // narration
                                "INCOME_VOUCHER", // referenceType
                                voucher.getId(), // referenceId
                                voucher.getAmount(), // totalAmount

                                request.getSocietyId(), // societyId

                                "CASH".equalsIgnoreCase(voucher.getPaymentMode())
                                            ? request.getGlCashInHand() : request.getGlBankAccount(),   

                                voucher.getAmount(),

                                voucher.getIncomeGlCode(), // Credit Income ✅
                                voucher.getAmount(),

                                "INCOME",
                                voucher.getId(),
                                voucher.getFinancialYearId(),

                                0L, // createdBy
                                null, // flatId
                                null // member
                                );

        voucher.setJournalId(journalId);

        return incomeVoucherRepository.save(voucher);
    }

    public List<IncomeVoucher> getAll(Long societyId, Long financialYearId) {

        return incomeVoucherRepository
                .findBySocietyIdAndFinancialYearIdOrderByVoucherDateDesc(
                        societyId,
                        financialYearId);
    }

    public IncomeVoucher getById(Long id) {

        return incomeVoucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income Voucher not found"));
    }

    public void delete(Long id) {

        IncomeVoucher voucher = getById(id);

        Long journalId = voucher.getJournalId();

        if (journalId != null) {

            journalEntryLineRepository.deleteByJournalId(journalId);

            journalEntryRepository.deleteById(journalId);
        }

        incomeVoucherRepository.delete(voucher);
    }


}
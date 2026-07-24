package com.society.backend.gl.service;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import com.society.backend.entity.Society;
import com.society.backend.gl.dto.TransferRequest;
import com.society.backend.gl.entity.TransferVoucher;
import com.society.backend.gl.repository.JournalEntryLineRepository;
import com.society.backend.gl.repository.JournalEntryRepository;
import com.society.backend.repository.SocietyRepository;
import com.society.backend.repository.TransferRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class TransferService {

    private final TransferRepository transferVoucherRepository;
    private final SocietyRepository societyRepository;
    private final JournalService journalService;
    private final JournalEntryRepository journalEntryRepository;
    private final JournalEntryLineRepository journalEntryLineRepository;

    public TransferService(
            TransferRepository transferVoucherRepository,
            SocietyRepository societyRepository,
            JournalService journalService,
            JournalEntryRepository journalEntryRepository,
            JournalEntryLineRepository journalEntryLineRepository) {

        this.transferVoucherRepository = transferVoucherRepository;
        this.societyRepository = societyRepository;
        this.journalService = journalService;
        this.journalEntryRepository = journalEntryRepository;
        this.journalEntryLineRepository = journalEntryLineRepository;
    }

    public TransferVoucher save(TransferRequest request) {
        Society society = societyRepository.findById(request.getSocietyId())
                .orElseThrow(() -> new RuntimeException("Society not found"));

        if (request.getFromGlCode().equals(request.getToGlCode())) {
            throw new RuntimeException("From Account and To Account cannot be the same.");
        }

        TransferVoucher voucher = new TransferVoucher();

        voucher.setSociety(society);
        voucher.setVoucherDate(request.getVoucherDate());
        voucher.setFromGlCode(request.getFromGlCode());
        voucher.setToGlCode(request.getToGlCode());
        voucher.setAmount(request.getAmount());
        voucher.setNarration(request.getNarration());
        voucher.setFinancialYearId(request.getFinancialYearId());

        // Generate Voucher No
        voucher.setVoucherNo("TRF-" + System.currentTimeMillis());

        // Save first so voucher ID is generated
        voucher = transferVoucherRepository.save(voucher);

        // Create Journal Entry
        Long journalId = journalService.createJournalEntry(

                voucher.getVoucherNo(),      // voucherNo
                "TRANSFER",                  // voucherType
                voucher.getNarration(),      // narration
                "TRANSFER_VOUCHER",          // referenceType
                voucher.getId(),             // referenceId
                voucher.getAmount(),         // totalAmount

                request.getSocietyId(),

                request.getFromGlCode(),     // Debit Account
                voucher.getAmount(),

                request.getToGlCode(),       // Credit Account
                voucher.getAmount(),

                "TRANSFER",
                voucher.getId(),
                voucher.getFinancialYearId(),

                0L,
                null,
                null,
                LocalDate.now()
        );

        voucher.setJournalId(journalId);

        return transferVoucherRepository.save(voucher);
    }

    public List<TransferVoucher> getAll(Long societyId, Long financialYearId) {

        return transferVoucherRepository
                .findBySocietyIdAndFinancialYearIdOrderByVoucherDateDesc(
                        societyId,
                        financialYearId);
    }

    public TransferVoucher getById(Long id) {

        return transferVoucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transfer Voucher not found"));
    }

    public void delete(Long id) {

        TransferVoucher voucher = getById(id);

        Long journalId = voucher.getJournalId();

        if (journalId != null) {
            journalEntryLineRepository.deleteByJournalId(journalId);
            journalEntryRepository.deleteById(journalId);
        }

        transferVoucherRepository.delete(voucher);
    }
}
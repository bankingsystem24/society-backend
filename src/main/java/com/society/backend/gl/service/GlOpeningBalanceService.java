package com.society.backend.gl.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

import com.society.backend.entity.Society;
import com.society.backend.gl.dto.GlOpeningBalanceRequest;
import com.society.backend.gl.entity.GlOpeningBalance;
import com.society.backend.gl.entity.JournalEntry;
import com.society.backend.gl.entity.JournalEntryLine;
import com.society.backend.gl.enums.VoucherType;
import com.society.backend.gl.repository.GlOpeningBalanceRepository;
import com.society.backend.gl.repository.JournalEntryLineRepository;
import com.society.backend.gl.repository.JournalEntryRepository;
import com.society.backend.repository.SocietyRepository;

import jakarta.transaction.Transactional;

@Service
public class GlOpeningBalanceService {

    private final GlOpeningBalanceRepository glOpeningBalanceRepository;
    private final SocietyRepository societyRepository;
    private final JournalEntryLineRepository journalEntryLineRepository;
    private final JournalEntryRepository journalEntryRepository;

    public GlOpeningBalanceService(GlOpeningBalanceRepository glOpeningBalanceRepository,
            SocietyRepository societyRepository,
            JournalEntryLineRepository journalEntryLineRepository,
            JournalEntryRepository journalEntryRepository) {
        this.glOpeningBalanceRepository = glOpeningBalanceRepository;
        this.societyRepository = societyRepository;
        this.journalEntryLineRepository = journalEntryLineRepository;
        this.journalEntryRepository = journalEntryRepository;
    }

    // ================= GET ALL =================
    public List<GlOpeningBalance> getAllOpeningBySociety(Long societyId, Long financialYearId) {
        return glOpeningBalanceRepository
                .findBySociety_IdAndFinancialYearId(societyId, financialYearId);
    }

    @Transactional
    public GlOpeningBalance save(GlOpeningBalanceRequest request, Long societyId) {

        Society society = societyRepository.findById(societyId)
                .orElseThrow(() -> new RuntimeException("Society not found"));

        // Save Opening Balance
        GlOpeningBalance mainEntry = new GlOpeningBalance();
        mainEntry.setSociety(society);
        mainEntry.setFinancialYearId(request.getFinancialYearId());
        mainEntry.setGlCode(request.getGlCode());
        mainEntry.setOpeningDebit(request.getOpeningDebit());
        mainEntry.setOpeningCredit(request.getOpeningCredit());

        GlOpeningBalance savedMain = glOpeningBalanceRepository.save(mainEntry);

        // Create Opening Journal Entry (Single Entry)
        JournalEntry entry = new JournalEntry();
        entry.setVoucherNo("OB-" + savedMain.getId());
        entry.setEntryDate(LocalDate.now());
        entry.setVoucherType(VoucherType.OPENING);
        entry.setNarration("Opening Balance");
        entry.setReferenceType("OPENING_BALANCE");
        entry.setReferenceId(savedMain.getId());
        entry.setTotalAmount(
                request.getOpeningDebit() > 0
                        ? request.getOpeningDebit()
                        : request.getOpeningCredit());
        entry.setStatus("POSTED");
        entry.setSocietyId(societyId);
        entry.setCreatedAt(LocalDateTime.now());
        entry.setCreatedBy(request.getCreatedBy());
        entry.setFinancialYearId(request.getFinancialYearId());

        JournalEntry savedEntry = journalEntryRepository.save(entry);

        // Create Single Journal Line
        JournalEntryLine line = new JournalEntryLine();
        line.setJournalEntry(savedEntry);
        line.setLineNo(1);
        line.setGlCode(request.getGlCode());

        if (request.getOpeningDebit() != null && request.getOpeningDebit() > 0) {
            line.setDebitAmount(request.getOpeningDebit());
            line.setCreditAmount(0.0);
            line.setRemarks("Opening Debit");
        } else {
            line.setDebitAmount(0.0);
            line.setCreditAmount(request.getOpeningCredit());
            line.setRemarks("Opening Credit");
        }

        line.setEntityType("SOCIETY");
        line.setEntityId(null);
        line.setSocietyId(societyId);
        line.setFlatId(null);
        line.setMember(null);
        line.setFinancialYearId(request.getFinancialYearId());

        journalEntryLineRepository.save(line);

        return savedMain;
    }

    // ================= UPDATE =================
    public GlOpeningBalance update(Long id, GlOpeningBalanceRequest request) {

        GlOpeningBalance existing = glOpeningBalanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));

        Society society = societyRepository.findById(request.getSocietyId())
                .orElseThrow(() -> new RuntimeException("Society not found"));

        existing.setSociety(society);
        existing.setFinancialYearId(request.getFinancialYearId());
        existing.setGlCode(request.getGlCode());
        existing.setOpeningDebit(request.getOpeningDebit());
        existing.setOpeningCredit(request.getOpeningCredit());
        existing.setOpeningBalance(request.getOpeningBalance());

        return glOpeningBalanceRepository.save(existing);
    }

    // ================= DELETE =================
    public void delete(Long id) {
        glOpeningBalanceRepository.deleteById(id);
    }
}

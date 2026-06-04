package com.society.backend.gl.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.society.backend.gl.entity.GlOpeningBalance;
import com.society.backend.gl.repository.GlOpeningBalanceRepository;

@Service
public class GlOpeningBalanceService {

    @Autowired
    private GlOpeningBalanceRepository glOpeningBalanceRepository;

    // ================= GET ALL =================
    public List<GlOpeningBalance> getAllOpeningBySociety(Long societyId) {
        return glOpeningBalanceRepository.findBySocietyId(societyId);
    }

    // ================= CREATE =================
    public GlOpeningBalance save(GlOpeningBalance entity) {

        // optional safety: prevent duplicate GL per FY
        GlOpeningBalance existing = glOpeningBalanceRepository
                .findBySocietyIdAndGlCodeAndFinancialYearId(
                        entity.getSociety().getId(),
                        entity.getGlCode(),
                        entity.getFinancialYearId()
                );

        if (existing != null) {
            throw new RuntimeException("Opening balance already exists for this GL and year");
        }

        return glOpeningBalanceRepository.save(entity);
    }

    // ================= UPDATE =================
    public GlOpeningBalance update(Long id, GlOpeningBalance entity) {

        GlOpeningBalance existing = glOpeningBalanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opening Balance not found"));

        existing.setGlCode(entity.getGlCode());
        existing.setOpeningDebit(entity.getOpeningDebit());
        existing.setOpeningCredit(entity.getOpeningCredit());

        double balance = (entity.getOpeningDebit() != null ? entity.getOpeningDebit() : 0)
                       - (entity.getOpeningCredit() != null ? entity.getOpeningCredit() : 0);

        existing.setOpeningBalance(balance);
        existing.setFinancialYearId(entity.getFinancialYearId());
        existing.setSociety(entity.getSociety());

        return glOpeningBalanceRepository.save(existing);
    }

    // ================= DELETE =================
    public void delete(Long id) {
        glOpeningBalanceRepository.deleteById(id);
    }
}

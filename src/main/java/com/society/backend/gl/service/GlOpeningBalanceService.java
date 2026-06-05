package com.society.backend.gl.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.society.backend.entity.Society;
import com.society.backend.gl.dto.GlOpeningBalanceRequest;
import com.society.backend.gl.entity.GlOpeningBalance;
import com.society.backend.gl.repository.GlOpeningBalanceRepository;
import com.society.backend.repository.SocietyRepository;

@Service
public class GlOpeningBalanceService {

    @Autowired
    private GlOpeningBalanceRepository glOpeningBalanceRepository;

    @Autowired
    private SocietyRepository societyRepository;

    // ================= GET ALL =================
    public List<GlOpeningBalance> getAllOpeningBySociety(Long societyId) {
        return glOpeningBalanceRepository.findBySocietyId(societyId);
    }

    public GlOpeningBalance save(GlOpeningBalance entity, Long societyId) {

        Society society = societyRepository.findById(societyId)
                .orElseThrow(() -> new RuntimeException("Society not found"));

        entity.setSociety(society);

        return glOpeningBalanceRepository.save(entity);
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

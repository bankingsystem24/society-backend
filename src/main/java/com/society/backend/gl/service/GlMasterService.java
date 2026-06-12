package com.society.backend.gl.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.society.backend.gl.entity.GlMaster;
import com.society.backend.gl.repository.GlMasterRepository;

@Service
public class GlMasterService {

    @Autowired
    private GlMasterRepository glMasterRepository;

    public List<GlMaster> getAllBySociety(Long societyId) {
        return glMasterRepository.findBySocietyIdOrderByGlCodeAsc(societyId);
    }

    public GlMaster save(GlMaster glMaster) {
        return glMasterRepository.save(glMaster);
    }

    public GlMaster update(
            Integer glCode,
            Long societyId,
            GlMaster glMaster) {

        GlMaster existing = glMasterRepository.findByGlCodeAndSocietyId(
                glCode,
                societyId);

        if (existing == null) {
            throw new RuntimeException("GL Master not found");
        }

        existing.setAccountName(glMaster.getAccountName());
        existing.setGroupName(glMaster.getGroupName());
        existing.setAccountType(glMaster.getAccountType());
        existing.setParentGlCode(glMaster.getParentGlCode());
        existing.setIsActive(glMaster.getIsActive());

        return glMasterRepository.save(existing);
    }

    public void delete(
            Integer glCode,
            Long societyId) {

        GlMaster existing = glMasterRepository.findByGlCodeAndSocietyId(
                glCode,
                societyId);

        if (existing == null) {
            throw new RuntimeException("GL Master not found");
        }

        glMasterRepository.delete(existing);
    }

}
package com.society.backend.gl.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.society.backend.gl.entity.GlMapping;
import com.society.backend.gl.entity.GlMaster;
import com.society.backend.gl.repository.GlMappingRepository;
import com.society.backend.gl.repository.GlMasterRepository;

@Service
public class GlMasterService {

    private final GlMasterRepository glMasterRepository;
    private final GlMappingRepository glMappingRepository;

    public GlMasterService(GlMasterRepository glMasterRepository,GlMappingRepository glMappingRepository){
        this.glMasterRepository = glMasterRepository;
        this.glMappingRepository = glMappingRepository;
    };

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

    public GlMapping save(GlMapping glMapping) {
        return glMappingRepository.save(glMapping);
    }

    public List<GlMapping> getMappingBySociety(Long societyId) {
        return glMappingRepository.findBySocietyId(societyId);
    }
}
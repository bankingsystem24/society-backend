package com.society.backend.gl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.society.backend.gl.entity.GlMaster;

public interface GlMasterRepository
        extends JpaRepository<GlMaster, Long> {
     GlMaster findByGlCodeAndSocietyId(Integer glCode,Long societyId);
    List<GlMaster> findBySocietyIdOrderByGlCodeAsc(Long societyId);
    GlMaster findBySocietyIdAndGlCode(Long societyId, Integer glCode);
    List<GlMaster> findBySocietyIdAndIsActiveTrue(Long societyId);
    List<GlMaster> findBySocietyIdAndGroupName(Long societyId,String groupName);

}
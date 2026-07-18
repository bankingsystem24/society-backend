package com.society.backend.gl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.society.backend.gl.entity.GlMaster;

public interface GlMasterRepository
        extends JpaRepository<GlMaster, Long> {
     GlMaster findByGlCodeAndSocietyId(Integer glCode,Long societyId);
    List<GlMaster> findBySocietyIdOrderByGlCodeAsc(Long societyId);
    GlMaster findBySocietyIdAndGlCode(Long societyId, Integer glCode);
    List<GlMaster> findBySocietyIdAndIsActiveTrue(Long societyId);
    List<GlMaster> findBySocietyIdAndGroupName(Long societyId,String groupName);

    @Query("""
        SELECT g
        FROM GlMaster g
        WHERE g.societyId = :societyId
        AND g.isActive = true
        AND g.glCode IN (1001,1002,1003)
        ORDER BY g.glCode
    """)
    List<GlMaster> findCashBankAccounts(@Param("societyId") Long societyId);

}
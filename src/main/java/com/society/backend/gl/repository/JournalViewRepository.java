package com.society.backend.gl.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.society.backend.gl.dto.JournalViewDTO;
import com.society.backend.gl.entity.JournalEntry;

public interface JournalViewRepository
        extends JpaRepository<JournalEntry, Long> {

@Query("""
SELECT new com.society.backend.gl.dto.JournalViewDTO(
    je.id,
    je.voucherNo,
    CAST(je.voucherType as string),
    je.entryDate,
    je.narration,
    jl.glCode,
    gm.accountName,
    jl.debitAmount,
    jl.creditAmount,
    jl.entityType,
    jl.entityId,
    1
)
FROM JournalEntry je
JOIN je.lines jl
JOIN GlMaster gm ON gm.glCode = jl.glCode
WHERE je.societyId = :societyId
  AND je.entryDate BETWEEN :startDate AND :endDate
ORDER BY je.id DESC, jl.lineNo ASC
""")
    List<JournalViewDTO> getJournalView(
            @Param("societyId") Long societyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
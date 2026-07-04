package com.society.backend.gl.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.society.backend.gl.dto.LedgerDTO;
import com.society.backend.gl.entity.JournalEntry;

public interface LedgerRepository
        extends JpaRepository<JournalEntry, Long> {

@Query("""
SELECT new com.society.backend.gl.dto.LedgerDTO(
    je.entryDate,
    je.voucherNo,
    CAST(je.voucherType AS string),
    je.narration,
    jl.debitAmount,
    jl.creditAmount,
    0.0,
    jl.glCode,
    gm.accountName,
    ''
)
FROM JournalEntry je
JOIN je.lines jl
JOIN GlMaster gm ON gm.glCode = jl.glCode
WHERE je.societyId = :societyId
  AND jl.glCode = :glCode
  AND je.entryDate BETWEEN :startDate AND :endDate
ORDER BY je.entryDate, je.id
""")

List<LedgerDTO> getLedger(
            @Param("societyId") Long societyId,
            @Param("glCode") Integer glCode,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("financialYearId") Long financialYearId);

@Query("""
    SELECT 
        CASE 
            WHEN g.openingDebit IS NOT NULL AND g.openingDebit <> 0 
                THEN COALESCE(g.openingDebit, 0)
            ELSE 
                COALESCE(-g.openingCredit, 0)
        END
    FROM GlOpeningBalance g
    WHERE g.society.id = :societyId
      AND g.glCode = :glCode
      AND g.financialYearId = :financialYearId
""")

Double getOpeningBalance(
    @Param("societyId") Long societyId,
    @Param("glCode") Integer glCode,
    @Param("financialYearId") Long financialYearId
);
}
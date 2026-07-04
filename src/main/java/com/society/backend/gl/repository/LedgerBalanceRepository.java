package com.society.backend.gl.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.society.backend.gl.dto.TrialBalanceDTO;
import com.society.backend.gl.entity.LedgerBalance;

public interface LedgerBalanceRepository
        extends JpaRepository<LedgerBalance, Long> {

    Optional<LedgerBalance> findBySocietyIdAndGlCodeAndEntityIdAndEntityType(
            Long societyId,
            Integer glCode,
            Long entityId,
            String entityType);

    Optional<LedgerBalance> findBySocietyIdAndGlCode(
        Long societyId,
        Integer glCode);

@Query("""
SELECT new com.society.backend.gl.dto.TrialBalanceDTO(
    gm.glCode,
    gm.accountName,
    CAST(COALESCE(ob.openingDebit, 0) AS double),
    CAST(COALESCE(ob.openingCredit, 0) AS double),
    CAST(COALESCE(SUM(jl.debitAmount), 0) AS double),
    CAST(COALESCE(SUM(jl.creditAmount), 0) AS double),
    gm.groupName
)
FROM GlMaster gm
LEFT JOIN GlOpeningBalance ob
    ON ob.glCode = gm.glCode
   AND ob.society.id = :societyId
   AND ob.financialYearId = :financialYearId

LEFT JOIN JournalEntryLine jl
    ON jl.glCode = gm.glCode
   AND jl.societyId = :societyId
   AND jl.financialYearId = :financialYearId

GROUP BY
    gm.glCode,
    gm.accountName,
    gm.groupName,
    ob.openingDebit,
    ob.openingCredit

ORDER BY gm.glCode
""")

    List<TrialBalanceDTO> getTrialBalance(
            @Param("societyId") Long societyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("financialYearId") Long financialYearId);

}
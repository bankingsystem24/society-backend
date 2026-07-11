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

    CAST(COALESCE(SUM(
        CASE
            WHEN je.voucherType = com.society.backend.gl.enums.VoucherType.OPENING
            THEN jl.debitAmount
            ELSE 0
        END
    ),0) AS double),

    CAST(COALESCE(SUM(
        CASE
            WHEN je.voucherType = com.society.backend.gl.enums.VoucherType.OPENING
            THEN jl.creditAmount
            ELSE 0
        END
    ),0) AS double),

    CAST(COALESCE(SUM(
        CASE
            WHEN je.voucherType <> com.society.backend.gl.enums.VoucherType.OPENING
                 AND je.entryDate BETWEEN :startDate AND :endDate
            THEN jl.debitAmount
            ELSE 0
        END
    ),0) AS double),

    CAST(COALESCE(SUM(
        CASE
            WHEN je.voucherType <> com.society.backend.gl.enums.VoucherType.OPENING
                 AND je.entryDate BETWEEN :startDate AND :endDate
            THEN jl.creditAmount
            ELSE 0
        END
    ),0) AS double),

    gm.groupName
)
FROM GlMaster gm

LEFT JOIN JournalEntryLine jl
       ON jl.glCode = gm.glCode
      AND jl.societyId = :societyId
      AND jl.financialYearId = :financialYearId

LEFT JOIN jl.journalEntry je

GROUP BY
    gm.glCode,
    gm.accountName,
    gm.groupName

ORDER BY gm.glCode
""")
List<TrialBalanceDTO> getTrialBalance(
        @Param("societyId") Long societyId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("financialYearId") Long financialYearId);

        
}
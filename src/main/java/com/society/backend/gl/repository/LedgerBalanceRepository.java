package com.society.backend.gl.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.society.backend.gl.dto.TrialBalanceDTO;
import com.society.backend.gl.entity.LedgerBalance;

public interface LedgerBalanceRepository
        extends JpaRepository<LedgerBalance, Long> {

    Optional<LedgerBalance> findBySocietyIdAndGlCodeAndEntityIdAndEntityType(
            Long societyId,
            Integer glCode,
            Long entityId,
            String entityType
    );

    @Query("""
    SELECT new com.society.backend.gl.dto.TrialBalanceDTO(
        gm.glCode,
        gm.accountName,
        SUM(COALESCE(jl.debitAmount,0)),
        SUM(COALESCE(jl.creditAmount,0))
    )
    FROM JournalEntry je
    JOIN JournalEntryLine jl
        ON je.id = jl.journalId
    JOIN GlMaster gm
        ON gm.glCode = jl.glCode
    WHERE je.societyId = :societyId
    AND je.entryDate BETWEEN :startDate AND :endDate
    GROUP BY gm.glCode, gm.accountName
    ORDER BY gm.glCode
    """)
    List<TrialBalanceDTO> getTrialBalance(
            Long societyId,
            LocalDate startDate,
            LocalDate endDate
    );



}
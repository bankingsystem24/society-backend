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
        CAST(je.voucherType as string),
        je.narration,
        jl.debitAmount,
        jl.creditAmount,
        0.0,
        jl.glCode,
        gm.accountName
    )
    FROM JournalEntry je
    JOIN JournalEntryLine jl ON je.id = jl.journalId
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
            @Param("endDate") LocalDate endDate
    );

}
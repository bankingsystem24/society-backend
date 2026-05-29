package com.society.backend.gl.repository;

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
            jl.entityId
        )
        FROM JournalEntry je
        JOIN JournalEntryLine jl
            ON je.id = jl.journalId
        JOIN GlMaster gm
            ON gm.glCode = jl.glCode
        WHERE je.societyId = :societyId
        ORDER BY je.id DESC, jl.lineNo ASC
    """)
    List<JournalViewDTO> getJournalView(
            @Param("societyId") Long societyId);
}
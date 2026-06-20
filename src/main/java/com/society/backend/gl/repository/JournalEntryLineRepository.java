package com.society.backend.gl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.society.backend.gl.entity.JournalEntryLine;

public interface JournalEntryLineRepository
        extends JpaRepository<JournalEntryLine, Long> {

    List<JournalEntryLine> findByJournalEntryId(Long journalId);


    @Query(value = """
    SELECT gm.gl_code, gm.account_name, gm.account_type,
        COALESCE(SUM(jel.debit_amount),0) AS debit_total,
        COALESCE(SUM(jel.credit_amount),0) AS credit_total
    FROM journal_entry_line jel
    JOIN gl_master gm ON gm.gl_code = jel.gl_code
    WHERE jel.society_id = :societyId AND jel.financial_year_id = :financialYearId
    GROUP BY gm.gl_code, gm.account_name, gm.account_type
""", nativeQuery = true)
List<Object[]> getProfitLossData(Long societyId,Long financialYearId);


}
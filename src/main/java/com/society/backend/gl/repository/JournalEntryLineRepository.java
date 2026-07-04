package com.society.backend.gl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.society.backend.gl.entity.JournalEntryLine;

import jakarta.transaction.Transactional;

public interface JournalEntryLineRepository
        extends JpaRepository<JournalEntryLine, Long> {
 
    List<JournalEntryLine> findByJournalEntry_Id(Long journalId);

@Modifying
@Transactional
@Query("DELETE FROM JournalEntryLine j WHERE j.journalEntry.id = :journalId")
int deleteByJournalId(@Param("journalId") Long journalId);



    @Query(value = """
    SELECT gm.gl_code, gm.account_name, gm.group_name,
        COALESCE(SUM(jel.debit_amount),0) AS debit_total,
        COALESCE(SUM(jel.credit_amount),0) AS credit_total
    FROM journal_entry_line jel
    JOIN gl_master gm ON gm.gl_code = jel.gl_code
    WHERE jel.society_id = :societyId AND jel.financial_year_id = :financialYearId
    GROUP BY gm.gl_code, gm.account_name, gm.group_name
""", nativeQuery = true)
List<Object[]> getProfitLossData(Long societyId,Long financialYearId);

@Query("""
SELECT
l.glCode,
COALESCE(SUM(l.debitAmount),0),
COALESCE(SUM(l.creditAmount),0)
FROM JournalEntryLine l
WHERE l.societyId=:societyId
AND l.financialYearId=:fyId
GROUP BY l.glCode
""")
List<Object[]> getTransactionTotals(Long societyId, Long fyId);


}
package com.society.backend.gl.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.society.backend.gl.entity.JournalEntry;
import com.society.backend.gl.entity.JournalEntryLine;

import jakarta.transaction.Transactional;

public interface JournalEntryLineRepository extends JpaRepository<JournalEntryLine, Long> {

    List<JournalEntryLine> findByJournalEntry_Id(Long journalId);

    @Modifying
    @Transactional
    @Query("DELETE FROM JournalEntryLine j WHERE j.journalEntry.id = :journalId")
    int deleteByJournalId(@Param("journalId") Long journalId);

    void deleteByJournalEntry(JournalEntry journalEntry);

    @Query(value = """
            SELECT gm.gl_code, gm.account_name, gm.group_name,
                COALESCE(SUM(jel.debit_amount),0) AS debit_total,
                COALESCE(SUM(jel.credit_amount),0) AS credit_total
            FROM journal_entry_line jel
            JOIN gl_master gm ON gm.gl_code = jel.gl_code
            WHERE jel.society_id = :societyId AND jel.financial_year_id = :financialYearId
            GROUP BY gm.gl_code, gm.account_name, gm.group_name
            """, nativeQuery = true)
    List<Object[]> getProfitLossData(Long societyId, Long financialYearId);

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

    @Query(value = """
            SELECT
                je.entry_date,
                je.voucher_no,

                CASE
                    WHEN jl2.member_id IS NOT NULL
                        THEN CONCAT(fl.flat_no, ' - ', m.name)
                    ELSE gm.account_name
                END AS particulars,

                jl.remarks,
                jl.debit_amount,
                jl.credit_amount

            FROM journal_entry_line jl

            JOIN journal_entry je
                ON je.id = jl.journal_id

            LEFT JOIN journal_entry_line jl2
                ON jl2.journal_id = jl.journal_id
               AND jl2.id <> jl.id

            LEFT JOIN members m
                ON m.id = jl2.member_id

            LEFT JOIN flats fl
                ON fl.id = jl2.flat_id

            LEFT JOIN gl_master gm
                ON gm.gl_code = jl2.gl_code

            WHERE jl.society_id = :societyId
            AND jl.gl_code = :accountCode
            AND je.entry_date BETWEEN :fromDate AND :toDate

            GROUP BY
                jl.id,
                je.entry_date,
                je.voucher_no,
                particulars,
                jl.remarks,
                jl.debit_amount,
                jl.credit_amount

            ORDER BY je.entry_date, jl.id
            """, nativeQuery = true)
    List<Object[]> getCashBook(
            Long societyId,
            Integer accountCode,
            LocalDate fromDate,
            LocalDate toDate);

    @Query(value = """
            SELECT
            COALESCE(SUM(jl.debit_amount),0)-COALESCE(SUM(jl.credit_amount),0)
            FROM journal_entry_line jl
            JOIN journal_entry je
            ON jl.journal_id=je.id
            WHERE jl.society_id=:societyId
            AND jl.gl_code=:accountCode
            AND je.entry_date<:fromDate
            """, nativeQuery = true)
    BigDecimal getOpeningBalance(Long societyId, Integer accountCode, LocalDate fromDate);

}
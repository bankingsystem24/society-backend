package com.society.backend.gl.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.society.backend.gl.dto.DayBookDTO;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DayBookRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<DayBookDTO> getDayBook(Long societyId, LocalDate date) {

        String sql = """
                SELECT
                    je.entry_date,
                    je.voucher_no,
                    jl.gl_code,
                    gm.account_name,
                    jl.flat_id,
                    m.name AS member_name,
                    jl.debit_amount,
                    jl.credit_amount,
                    jl.remarks,
                    jl.entity_type,
                    je.narration
                FROM journal_entry_line jl
                JOIN journal_entry je
                    ON jl.journal_id = je.id
                LEFT JOIN gl_master gm
                    ON gm.gl_code = jl.gl_code
                LEFT JOIN flats fl
                    ON fl.id = jl.flat_id
                LEFT JOIN members m
                    ON m.id = fl.owner_member_id
                WHERE je.entry_date = ?
                  AND jl.society_id = ?
                  AND jl.entity_type <> 'MEMBER'
                ORDER BY
                    jl.gl_code,
                    je.entry_date,
                    jl.id
                """;

        return jdbcTemplate.query(sql, new DayBookMapper(), date, societyId);
    }

    private static class DayBookMapper implements RowMapper<DayBookDTO> {

        @Override
        public DayBookDTO mapRow(ResultSet rs, int rowNum) throws SQLException {

            DayBookDTO dto = new DayBookDTO();

            dto.setEntryDate(rs.getDate("entry_date").toLocalDate());
            dto.setVoucherNo(rs.getString("voucher_no"));
            dto.setGlCode(rs.getInt("gl_code"));
            dto.setAccountName(rs.getString("account_name"));
            dto.setFlatId(rs.getLong("flat_id"));
            dto.setMemberName(rs.getString("member_name"));
            dto.setDebitAmount(rs.getDouble("debit_amount"));
            dto.setCreditAmount(rs.getDouble("credit_amount"));
            dto.setRemarks(rs.getString("remarks"));
            dto.setEntityType(rs.getString("entity_type"));
            dto.setParticulars(rs.getString("narration"));

            return dto;
        }
    }

    public Double getOpeningBalance(Long societyId, LocalDate date) {

    String sql = """
        SELECT COALESCE(
            SUM(jl.debit_amount - jl.credit_amount), 0
        )
        FROM journal_entry_line jl
        JOIN journal_entry je
            ON je.id = jl.journal_id
        WHERE jl.society_id = ?
          AND je.entry_date < ?
          AND jl.gl_code IN (1001,1002,1003)
    """;

    return jdbcTemplate.queryForObject(
            sql,
            Double.class,
            societyId,
            date
    );
}

}
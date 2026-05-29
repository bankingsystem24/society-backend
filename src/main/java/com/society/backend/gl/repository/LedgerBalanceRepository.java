package com.society.backend.gl.repository;

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
    l.glCode,
    g.accountName,
    SUM(l.debit),
    SUM(l.credit),
    SUM(l.debit - l.credit),
    g.groupName,
    CASE WHEN SUM(l.debit - l.credit) >= 0 THEN 'DEBIT' ELSE 'CREDIT' END
)
FROM LedgerBalance l
JOIN GlMaster g ON l.glCode = g.glCode
WHERE l.societyId = :societyId
GROUP BY l.glCode, g.accountName, g.groupName
""")
List<TrialBalanceDTO> getTrialBalance(Long societyId);



}
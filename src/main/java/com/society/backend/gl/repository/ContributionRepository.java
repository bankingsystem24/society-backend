package com.society.backend.gl.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.society.backend.gl.entity.Contribution;

public interface ContributionRepository extends JpaRepository<Contribution, Long> {

    List<Contribution> findBySocietyIdAndTypeAndFinancialYearId(Long societyId, String type,Long financialYearId);

    List<Contribution> findBySocietyIdAndFinancialYearId(Long societyId,Long financialYearId);


    List<Contribution> findByReceiptId(Long receiptId);
}

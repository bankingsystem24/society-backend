package com.society.backend.gl.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.society.backend.gl.entity.Contribution;

public interface ContributionRepository extends JpaRepository<Contribution, Long> {

    List<Contribution> findBySocietyIdAndTypeAndFinancialYearId(Long societyId, String type,Long financialYearId);

    List<Contribution> findBySocietyIdAndFinancialYearId(Long societyId,Long financialYearId);

    List<Contribution> findBySocietyIdAndFinancialYearIdAndType(Long societyId,Long financialYearId,String type);

    List<Contribution> findBySocietyIdAndFinancialYearIdAndReceiptId(Long societyId,Long financialYearId,Long receiptId);


    List<Contribution> findByReceiptId(Long receiptId);

    List<Contribution> findByFlat_IdInAndSocietyIdAndFinancialYearId(List<Long> flatIds,Long societyId,Long financialYearId);

    List<Contribution> findBySociety_IdAndFlat_Id(Long societyId,Long flatId);

    List<Contribution> findByMemberIdAndSocietyIdAndFinancialYearId(Long memberId,Long societyId,Long financialYearId);

    List<Contribution> findByIdIn(List<Long> ids);



}

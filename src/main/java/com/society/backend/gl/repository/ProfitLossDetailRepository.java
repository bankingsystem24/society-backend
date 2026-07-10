package com.society.backend.gl.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.society.backend.gl.entity.ProfitLossDetail;

public interface ProfitLossDetailRepository extends JpaRepository<ProfitLossDetail, Long> {

    List<ProfitLossDetail> findByHeaderId(Long headerId);

    List<ProfitLossDetail> findByHeaderFinancialYearId(Long financialYearId);

}

package com.society.backend.gl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.society.backend.gl.entity.GlOpeningBalance;

@Repository
public interface GlOpeningBalanceRepository extends JpaRepository<GlOpeningBalance, Long> {

    List<GlOpeningBalance> findBySocietyId(Long societyId);

    GlOpeningBalance findBySocietyIdAndGlCodeAndFinancialYearId(
        Long societyId,
        Integer glCode,
        Long financialYearId
);


}

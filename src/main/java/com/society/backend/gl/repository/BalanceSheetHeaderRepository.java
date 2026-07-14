package com.society.backend.gl.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.society.backend.gl.entity.BalanceSheetHeader;

public interface BalanceSheetHeaderRepository extends JpaRepository<BalanceSheetHeader, Long> {

    Optional<BalanceSheetHeader> findByFinancialYearId(Long financialYearId);

    Optional<BalanceSheetHeader> findBySocietyIdAndFinancialYearId(
            Long societyId,
            Long financialYearId);

}
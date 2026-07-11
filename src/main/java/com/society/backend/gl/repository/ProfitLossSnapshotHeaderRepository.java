package com.society.backend.gl.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.society.backend.gl.entity.ProfitLossSnapshotHeader;

public interface ProfitLossSnapshotHeaderRepository
        extends JpaRepository<ProfitLossSnapshotHeader, Long> {

    Optional<ProfitLossSnapshotHeader> findByFinancialYearId(Long financialYearId);

    boolean existsByFinancialYearId(Long financialYearId);

    void deleteByFinancialYearId(Long financialYearId);

}
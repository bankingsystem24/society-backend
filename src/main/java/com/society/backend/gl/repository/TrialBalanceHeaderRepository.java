package com.society.backend.gl.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.society.backend.gl.entity.TrialBalanceHeader;

public interface TrialBalanceHeaderRepository
        extends JpaRepository<TrialBalanceHeader, Long> {

    Optional<TrialBalanceHeader> findByFinancialYearId(Long financialYearId);

}
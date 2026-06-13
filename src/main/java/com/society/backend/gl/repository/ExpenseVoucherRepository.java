package com.society.backend.gl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.society.backend.gl.entity.ExpenseVoucher;

public interface ExpenseVoucherRepository
        extends JpaRepository<ExpenseVoucher, Long> {

    List<ExpenseVoucher> findBySocietyIdAndFinancialYearIdOrderByVoucherDateDesc(Long societyId, Long financialYearId);}

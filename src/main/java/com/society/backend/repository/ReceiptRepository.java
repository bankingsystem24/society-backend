package com.society.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.society.backend.entity.Receipt;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    List<Receipt> findBySocietyIdAndFinancialYearId(Long societyId, Long financialYearId);

   List<Receipt> findByFinancialYearId(Long financialYearId);



}
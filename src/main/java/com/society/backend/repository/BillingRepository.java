package com.society.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.society.backend.entity.Billing;
import com.society.backend.entity.Flat;
import com.society.backend.entity.Society;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {

    // Get all bills of a society
    List<Billing> findBySocietyId(Long societyId);

    // Get all bills of a flat
    List<Billing> findByFlatId(Long flatId);

    // Get bills by society + month + year (VERY useful for reports)
    List<Billing> findBySocietyIdAndMonthAndYear(Long societyId, String month, int year);

    // Get unpaid bills (dashboard use)
    List<Billing> findBySocietyIdAndStatus(Long societyId, com.society.backend.enums.PaymentStatus status);

    // Optional: check duplicate bill generation
    boolean existsByFlatIdAndMonthAndYear(Long flatId, String month, int year);

    
}
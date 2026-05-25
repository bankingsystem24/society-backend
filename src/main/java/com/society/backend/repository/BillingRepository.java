package com.society.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.society.backend.entity.Billing;
import com.society.backend.enums.PaymentStatus;

public interface BillingRepository extends JpaRepository<Billing, Long> {

    // ✅ DUPLICATE CHECK
    boolean existsByFlatIdAndMonthAndYear(
            Long flatId,
            String month,
            int year
    );

    // ✅ SOCIETY BILLS
    List<Billing> findBySocietyId(Long societyId);

    // ✅ PENDING BILLS
    List<Billing> findBySocietyIdAndStatus(
            Long societyId,
            PaymentStatus status
    );

    // ✅ FILTER : FLAT
    List<Billing> findBySocietyIdAndFlatId(
            Long societyId,
            Long flatId
    );

    // ✅ FILTER : MONTH
    List<Billing> findBySocietyIdAndMonth(
            Long societyId,
            String month
    );

    // ✅ FILTER : FLAT + MONTH
    List<Billing> findBySocietyIdAndFlatIdAndMonth(
            Long societyId,
            Long flatId,
            String month
    );
}
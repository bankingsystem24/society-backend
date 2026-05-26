package com.society.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.society.backend.entity.Billing;
import com.society.backend.enums.PaymentStatus;

public interface BillingRepository extends JpaRepository<Billing, Long> {

    // ================= DUPLICATE CHECK =================
    boolean existsByFlatIdAndMonthAndYear(
            Long flatId,
            String month,
            int year
    );

    // ================= BASIC FETCH =================
    List<Billing> findBySocietyId(Long societyId);

    List<Billing> findBySocietyIdAndStatus(
            Long societyId,
            PaymentStatus status
    );

    // ================= FILTERS =================
    List<Billing> findBySocietyIdAndFlatId(
            Long societyId,
            Long flatId
    );

    List<Billing> findBySocietyIdAndMonth(
            Long societyId,
            String month
    );

    List<Billing> findBySocietyIdAndFlatIdAndMonth(
            Long societyId,
            Long flatId,
            String month
    );

    // ================= RECEIPT SUPPORT =================

    // Fetch multiple bills by IDs
    List<Billing> findByIdIn(List<Long> ids);

    // 🔥 IMPORTANT: for Member Dashboard (via Flat mapping)
   List<Billing> findByFlatIdIn(List<Long> flatIds);

    // Fetch by receipt
    List<Billing> findByReceiptId(Long receiptId);

    // ================= BULK UPDATE =================
    @Modifying
    @Transactional
    @Query("""
        UPDATE Billing b
        SET b.receiptId = :receiptId,
            b.status = :status,
            b.paidDate = CURRENT_DATE
        WHERE b.id IN :ids
    """)
    void updateReceiptAndStatus(
            Long receiptId,
            PaymentStatus status,
            List<Long> ids
    );
    
}
package com.society.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.society.backend.entity.Billing;
import com.society.backend.enums.PaymentStatus;
import com.society.backend.gl.enums.BillType;
 
public interface BillingRepository extends JpaRepository<Billing, Long> {

        // ================= DUPLICATE CHECK =================
        boolean existsByFlatIdAndMonthAndYear(
                        Long flatId,
                        String month,
                        int year);

        // ================= BASIC FETCH =================
        List<Billing> findBySocietyId(Long societyId); 

        List<Billing> findBySocietyIdAndFinancialYearId(Long societyId, Long financialYearId);

        List<Billing> findBySocietyIdAndStatus(
                        Long societyId,
                        PaymentStatus status);

        // ================= FILTERS =================
        List<Billing> findBySocietyIdAndFlatId(
                        Long societyId,
                        Long flatId);

        List<Billing> findBySocietyIdAndMonth(
                        Long societyId,
                        String month);

        List<Billing> findBySocietyIdAndFlatIdAndMonth(
                        Long societyId,
                        Long flatId,
                        String month);

        // ================= RECEIPT SUPPORT =================

        // Fetch multiple bills by IDs
        List<Billing> findByIdIn(List<Long> ids);

        // 🔥 IMPORTANT: for Member Dashboard (via Flat mapping)
        List<Billing> findByFlatIdInAndSocietyIdAndFinancialYearId(List<Long> flatIds, Long societyId,
                        Long financialYearId);

        // Fetch by receipt
        List<Billing> findByReceiptId(Long receiptId);

        @Modifying
        @Transactional
        @Query("""
                            UPDATE Billing b
                            SET b.receiptId = :receiptId,
                                b.status = :status,
                                b.transactionId = :transactionId,
                                b.paymentMode = :paymentMode,
                                b.paidDate = CURRENT_DATE
                            WHERE b.id IN :ids
                        """)
        void updateReceiptAndStatus(
                        Long receiptId,
                        PaymentStatus status,
                        String transactionId,
                        String paymentMode,
                        List<Long> ids);

        @Query("""
            select b
            from Billing b
            where b.society.id = :societyId
            and b.financialYearId = :financialYearId
            and b.billType = :billType
            order by b.flat.flatNo
            """)
        List<Billing> getArrears(
                Long societyId,
                Long financialYearId,
                BillType billType);


}
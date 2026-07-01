package com.society.backend.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.society.backend.gl.entity.TransferVoucher;

public interface TransferRepository extends JpaRepository<TransferVoucher, Long> {

    List<TransferVoucher> findBySocietyIdAndFinancialYearIdOrderByVoucherDateDesc(
            Long societyId,
            Long financialYearId
    );
}
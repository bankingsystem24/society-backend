package com.society.backend.gl.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.society.backend.gl.entity.IncomeVoucher;

public interface IncomeVoucherRepository extends JpaRepository<IncomeVoucher, Long> {

    List<IncomeVoucher> findBySocietyIdAndFinancialYearIdOrderByVoucherDateDesc(
            Long societyId,
            Long financialYearId);

}

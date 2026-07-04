package com.society.backend.gl.repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.society.backend.gl.entity.GlOpeningBalance;

public interface GlOpeningBalanceRepository
                extends JpaRepository<GlOpeningBalance, Long> {

        List<GlOpeningBalance> findBySociety_Id(Long societyId);

        Optional<GlOpeningBalance> findFirstBySociety_IdAndGlCodeAndFinancialYearId(
                        Long societyId,
                        Integer glCode,
                        Long financialYearId);
 
        List<GlOpeningBalance> findBySociety_IdAndFinancialYearId(
                        Long societyId,
                        Long financialYearId);


}
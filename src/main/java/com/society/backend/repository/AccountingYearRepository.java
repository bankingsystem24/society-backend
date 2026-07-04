package com.society.backend.repository;
import com.society.backend.entity.AccountingYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface AccountingYearRepository extends JpaRepository<AccountingYear, Long> {

    Optional<AccountingYear> findBySocietyIdAndIsActiveTrue(Long societyId);

    Optional<AccountingYear> findByIdAndSociety_Id(Long id, Long societyId);

    List<AccountingYear> findBySocietyId(Long societyId);

    List<AccountingYear> findAll();
}

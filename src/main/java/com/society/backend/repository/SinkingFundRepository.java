package com.society.backend.repository;

import com.society.backend.entity.SinkingFund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SinkingFundRepository extends JpaRepository<SinkingFund, Long> {

    List<SinkingFund> findBySociety_Id(Long societyId);

    List<SinkingFund> findBySocietyIdAndMonthAndYear(Long societyId, String month, int year);

    List<SinkingFund> findBySocietyIdAndFlat_Id(Long societyId, Long flatId);
}
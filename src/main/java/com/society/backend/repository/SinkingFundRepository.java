package com.society.backend.repository;

import com.society.backend.entity.SinkingFund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SinkingFundRepository extends JpaRepository<SinkingFund, Long> {

    List<SinkingFund> findBySociety_Id(Long societyId);

    List<SinkingFund> findBySocietyIdAndMonthAndYear(Long societyId, String month, int year);

    List<SinkingFund> findBySocietyIdAndFlat_Id(Long societyId, Long flatId);
}
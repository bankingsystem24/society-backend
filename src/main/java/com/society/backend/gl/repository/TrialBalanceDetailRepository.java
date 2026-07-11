package com.society.backend.gl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.society.backend.gl.entity.TrialBalanceDetail;

public interface TrialBalanceDetailRepository
        extends JpaRepository<TrialBalanceDetail, Long> {

    List<TrialBalanceDetail> findByHeader_Id(Long headerId);

}
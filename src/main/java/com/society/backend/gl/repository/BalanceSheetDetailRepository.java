package com.society.backend.gl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.society.backend.gl.entity.BalanceSheetDetail;
import com.society.backend.gl.entity.BalanceSheetHeader;

public interface BalanceSheetDetailRepository extends JpaRepository<BalanceSheetDetail, Long> {

    List<BalanceSheetDetail> findByHeader(BalanceSheetHeader header);

    void deleteByHeader(BalanceSheetHeader header);

}
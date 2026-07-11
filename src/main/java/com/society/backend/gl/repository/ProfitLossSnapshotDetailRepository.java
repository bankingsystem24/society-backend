package com.society.backend.gl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.society.backend.gl.entity.ProfitLossSnapshotDetail;

public interface ProfitLossSnapshotDetailRepository
        extends JpaRepository<ProfitLossSnapshotDetail, Long> {

    List<ProfitLossSnapshotDetail> findByHeaderId(Long headerId);

    void deleteByHeaderId(Long headerId);

}
package com.society.backend.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.society.backend.entity.Wing;

public interface WingRepository extends JpaRepository<Wing, Long> {

    List<Wing> findBySociety_Id(Long societyId);
}

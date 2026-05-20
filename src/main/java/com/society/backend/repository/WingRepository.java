package com.society.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.society.backend.entity.Wing;

public interface WingRepository extends JpaRepository<Wing, Long> {
    
}

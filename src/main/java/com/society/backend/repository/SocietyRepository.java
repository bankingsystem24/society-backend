package com.society.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.society.backend.entity.Society;

public interface SocietyRepository extends JpaRepository<Society, Long> {
    
}

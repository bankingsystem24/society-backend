package com.society.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.society.backend.entity.Flat;

public interface FlatRepository extends JpaRepository<Flat, Long> {
    
}

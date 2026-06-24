package com.society.backend.gl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.society.backend.gl.entity.GlMapping;
import java.util.List;


public interface GlMappingRepository extends JpaRepository<GlMapping, Long> {

    List<GlMapping> findBySocietyId(Long societyId);
    
}

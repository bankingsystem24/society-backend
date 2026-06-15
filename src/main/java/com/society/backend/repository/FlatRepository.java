package com.society.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.society.backend.entity.Flat;

public interface FlatRepository extends JpaRepository<Flat, Long> {
    List<Flat> findBySociety_Id(Long societyId);
    List<Flat> findBySociety_IdAndOwner_Id(Long societyId, Long ownerId);

    

}
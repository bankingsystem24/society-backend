package com.society.backend.gl.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.society.backend.gl.entity.SocietyBillingPolicy;

public interface SocietyBillingPolicyRepository
        extends JpaRepository<SocietyBillingPolicy, Long> {

    Optional<SocietyBillingPolicy> findBySocietyId(Long societyId);

    // OR if Society is a relation:
    Optional<SocietyBillingPolicy> findBySociety_Id(Long societyId);
}
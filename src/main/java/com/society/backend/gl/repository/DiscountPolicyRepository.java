package com.society.backend.gl.repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.society.backend.gl.entity.DiscountPolicy;
 
public interface DiscountPolicyRepository
        extends JpaRepository<DiscountPolicy, Long> {

    List<DiscountPolicy> findBySociety_IdAndActiveTrue(Long societyId);

    Optional<DiscountPolicy> findFirstBySocietyIdAndActiveTrue(Long societyId);
}

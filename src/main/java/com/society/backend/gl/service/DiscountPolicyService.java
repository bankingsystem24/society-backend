package com.society.backend.gl.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import com.society.backend.gl.entity.DiscountPolicy;
import com.society.backend.gl.repository.DiscountPolicyRepository;

public class DiscountPolicyService {

    private final DiscountPolicyRepository discountPolicyRepository;

    public DiscountPolicyService(DiscountPolicyRepository discountPolicyRepository){
        this.discountPolicyRepository = discountPolicyRepository;
    }
    
    public BigDecimal calculateDiscount(
        BigDecimal billAmount,
        LocalDate dueDate,
        LocalDate paymentDate,
        Long societyId) {

    Optional<DiscountPolicy> optPolicy =
            discountPolicyRepository
                    .findFirstBySocietyIdAndActiveTrue(societyId);

    if (optPolicy.isEmpty()) {
        return BigDecimal.ZERO;
    }

    DiscountPolicy policy = optPolicy.get();

    LocalDate paidBeforeDate =policy.getPaidBeforeDate();

    if (!paymentDate.isAfter(paidBeforeDate))
    {
        return billAmount
                .multiply(policy.getDiscountPercent())
                .divide(BigDecimal.valueOf(100));
    }

    return BigDecimal.ZERO;
}
}

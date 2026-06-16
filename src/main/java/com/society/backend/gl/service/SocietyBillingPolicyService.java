package com.society.backend.gl.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.society.backend.entity.Society;
import com.society.backend.gl.dto.SocietyBillingPolicyDTO;
import com.society.backend.gl.entity.SocietyBillingPolicy;
import com.society.backend.gl.repository.SocietyBillingPolicyRepository;
import com.society.backend.repository.SocietyRepository;

@Service
public class SocietyBillingPolicyService {

    private final SocietyBillingPolicyRepository policyRepository;
    private final SocietyRepository societyRepository;

    public SocietyBillingPolicyService(
            SocietyBillingPolicyRepository policyRepository,
            SocietyRepository societyRepository) {

        this.policyRepository = policyRepository;
        this.societyRepository = societyRepository;
    }

    public SocietyBillingPolicy save(SocietyBillingPolicyDTO dto) {

        Society society = societyRepository.findById(dto.getSocietyId())
                .orElseThrow(() -> new RuntimeException("Society not found"));

        SocietyBillingPolicy policy =
                policyRepository.findBySocietyId(dto.getSocietyId())
                        .orElse(new SocietyBillingPolicy());

        policy.setSociety(society);
        policy.setInterestRate(dto.getInterestRate());
        policy.setInterestType(dto.getInterestType());
        policy.setFinancialYearId(dto.getFinancialYearId());

        return policyRepository.save(policy);
    }

    public List<SocietyBillingPolicy> getAll() {
        return policyRepository.findAll();
    }

    public SocietyBillingPolicy getById(Long id) {
        return policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found"));
    }

    public SocietyBillingPolicy getBySociety(Long societyId) {
        return policyRepository.findBySocietyId(societyId)
                .orElseThrow(() -> new RuntimeException("Policy not found"));
    }

    public void delete(Long id) {
        policyRepository.deleteById(id);
    }
}
package com.society.backend.gl.controller;
import com.society.backend.entity.Society;
import com.society.backend.gl.dto.DiscountPolicyRequest;
import com.society.backend.gl.entity.DiscountPolicy;
import com.society.backend.gl.repository.DiscountPolicyRepository;
import com.society.backend.repository.SocietyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discount-policy")
public class DiscountPolicyController {

    private final DiscountPolicyRepository discountPolicyRepository;
    private final SocietyRepository societyRepository;

    public DiscountPolicyController(
            DiscountPolicyRepository discountPolicyRepository,
            SocietyRepository societyRepository) {
        this.discountPolicyRepository = discountPolicyRepository;
        this.societyRepository = societyRepository;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody DiscountPolicyRequest request) {

        Society society = societyRepository.findById(request.getSocietyId())
                .orElseThrow(() -> new RuntimeException("Society not found"));

        DiscountPolicy policy = new DiscountPolicy();
        policy.setPolicyName(request.getPolicyName());
        policy.setActive(request.getActive());
        policy.setPaidBeforeDate(request.getPaidBeforeDate());
        policy.setDiscountPercent(request.getDiscountPercent());
        policy.setEffectiveFrom(request.getEffectiveFrom());
        policy.setEffectiveTo(request.getEffectiveTo());
        policy.setSociety(society);

        return ResponseEntity.ok(
                discountPolicyRepository.save(policy)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountPolicy> getById(@PathVariable Long id) {

        DiscountPolicy policy = discountPolicyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        return ResponseEntity.ok(policy);
    }

    @GetMapping("/society/{societyId}")
    public ResponseEntity<List<DiscountPolicy>> getBySociety(
            @PathVariable Long societyId) {

        return ResponseEntity.ok(
                discountPolicyRepository.findBySociety_IdAndActiveTrue(societyId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody DiscountPolicyRequest request) {

        DiscountPolicy policy = discountPolicyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        policy.setPolicyName(request.getPolicyName());
        policy.setActive(request.getActive());
        policy.setPaidBeforeDate(request.getPaidBeforeDate());
        policy.setDiscountPercent(request.getDiscountPercent());
        policy.setEffectiveFrom(request.getEffectiveFrom());
        policy.setEffectiveTo(request.getEffectiveTo());

        return ResponseEntity.ok(
                discountPolicyRepository.save(policy)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        discountPolicyRepository.deleteById(id);

        return ResponseEntity.ok("Deleted Successfully");
    }
}

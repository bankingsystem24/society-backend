package com.society.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.society.backend.entity.Society;
import com.society.backend.repository.SocietyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SocietyService {

    private final SocietyRepository societyRepository;

    public Society save(Society society) {
        return societyRepository.save(society);
    }

    public List<Society> getAll() {
        return societyRepository.findAll();
    }

    public Society getById(Long id) {
        return societyRepository.findById(id).orElse(null);
    }

    public Society update(Long id, Society society) {

        Society existing = societyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Society not found"));

        existing.setSocietyName(society.getSocietyName());
        existing.setRegistrationNumber(society.getRegistrationNumber());
        existing.setAddress(society.getAddress());
        existing.setCity(society.getCity());
        existing.setState(society.getState());
        existing.setCountry(society.getCountry());

        // 🔥 THIS WAS MISSING
        existing.setPinCode(society.getPinCode());

        existing.setEmail(society.getEmail());
        existing.setMobile(society.getMobile());
        existing.setSecretaryName(society.getSecretaryName());

        // 🔥 AUDITOR UPDATE (important)
        existing.setAuditor(society.getAuditor());
        existing.setUpi1(
                Optional.ofNullable(society.getUpi1())
                        .orElse(existing.getUpi1()));

        existing.setUpi2(
                Optional.ofNullable(society.getUpi2())
                        .orElse(existing.getUpi2()));

        existing.setUpi1Active(
                Optional.ofNullable(society.getUpi1Active())
                        .orElse(existing.getUpi1Active()));

        existing.setUpi2Active(
                Optional.ofNullable(society.getUpi2Active())
                        .orElse(existing.getUpi2Active()));

        return societyRepository.save(existing);
    }

    public void delete(Long id) {
        societyRepository.deleteById(id);
    }

}

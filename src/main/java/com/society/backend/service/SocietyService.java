package com.society.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.society.backend.entity.Society;
import com.society.backend.repository.SocietyRepository;

@Service
public class SocietyService {

    @Autowired
    private SocietyRepository societyRepository;  
    
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

        return societyRepository.save(existing);
    }

    public void delete(Long id) {
        societyRepository.deleteById(id);
    }

}

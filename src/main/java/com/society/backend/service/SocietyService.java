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

        Society existing = societyRepository.findById(id).orElse(null);

        if (existing != null) {

            existing.setSocietyName(society.getSocietyName());
            existing.setAddress(society.getAddress());

            return societyRepository.save(existing);
        }

        return null;
    }

    public void delete(Long id) {
        societyRepository.deleteById(id);
    }

}

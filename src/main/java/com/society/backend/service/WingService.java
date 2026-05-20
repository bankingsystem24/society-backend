package com.society.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.society.backend.dto.WingResponse;
import com.society.backend.entity.Wing;
import com.society.backend.exception.ResourceNotFoundException;
import com.society.backend.repository.WingRepository;

@Service
public class WingService {
 
    @Autowired
    private WingRepository wingRepository;

    public Wing save(Wing wing) {
        return wingRepository.save(wing);
    }

    public List<WingResponse> getAll() {

        return wingRepository.findAll().stream().map(wing -> {

        WingResponse res = new WingResponse();

        res.setId(wing.getId());
        res.setWingName(wing.getWingName());
        res.setDescription(wing.getDescription());
        res.setTotalFloors(wing.getTotalFloors());
        res.setTotalFlats(wing.getTotalFlats());
        res.setActive(wing.getActive());

        if (wing.getSociety() != null) {
            res.setSocietyId(wing.getSociety().getId());
            res.setSocietyName(wing.getSociety().getSocietyName());
        }

        return res;

        }).toList();
    }

    public WingResponse getById(Long id) {

    Wing wing = wingRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Wing not found with id: " + id));

    WingResponse res = new WingResponse();

    res.setId(wing.getId());
    res.setWingName(wing.getWingName());
    res.setDescription(wing.getDescription());
    res.setTotalFloors(wing.getTotalFloors());
    res.setTotalFlats(wing.getTotalFlats());
    res.setActive(wing.getActive());

    if (wing.getSociety() != null) {
        res.setSocietyId(wing.getSociety().getId());
        res.setSocietyName(wing.getSociety().getSocietyName());
    }

    return res;
}

    public Wing update(Long id, Wing wing) {

        Wing existing = wingRepository.findById(id).orElse(null);

        if (existing != null) {

            existing.setWingName(wing.getWingName());
            existing.setSociety(wing.getSociety());

            return wingRepository.save(existing);
        }

        return null;
    }

    public void delete(Long id) {
        wingRepository.deleteById(id);
    }
    
}

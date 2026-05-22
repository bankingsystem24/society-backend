package com.society.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.society.backend.dto.WingRequest;
import com.society.backend.dto.WingResponse;
import com.society.backend.entity.Society;
import com.society.backend.entity.Wing;
import com.society.backend.exception.ResourceNotFoundException;
import com.society.backend.repository.SocietyRepository;
import com.society.backend.repository.WingRepository;

@Service
public class WingService {
 
    @Autowired
    private WingRepository wingRepository;
    @Autowired
    private SocietyRepository societyRepository;

    public Wing save(Wing wing) {
        return wingRepository.save(wing);
    }

public WingResponse createWing(WingRequest req) {

    Wing wing = new Wing();

    wing.setWingName(req.getWingName());
    wing.setDescription(req.getDescription());
    wing.setActive(req.getActive());
    wing.setTotalFlats(req.getTotal_flats());
    wing.setTotalFloors(req.getTotal_floors());

    if (req.getSociety() != null &&
        req.getSociety().getId() != null) {

        Society s = societyRepository.findById(
                req.getSociety().getId())
                .orElseThrow(() ->
                        new RuntimeException("Society not found"));

        wing.setSociety(s);
    }

    Wing saved = wingRepository.save(wing);

    return toResponse(saved);
}

    private WingResponse toResponse(Wing wing) {

        WingResponse res = new WingResponse();

        res.setId(wing.getId());
        res.setWingName(wing.getWingName());
        res.setDescription(wing.getDescription());
        res.setActive(wing.getActive());
        res.setTotal_flats(wing.getTotalFlats());
        res.setTotal_floors(wing.getTotalFloors());

        if (wing.getSociety() != null) {
            res.setSociety(wing.getSociety());
        }

        return res;
    }

    public List<WingResponse> getAllBySocietyId(Long societyId) {
        return wingRepository.findBySociety_Id(societyId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<WingResponse> getAll() {

        return wingRepository.findAll()
                .stream()
                .map(wing -> {

                    WingResponse res = new WingResponse();

                    res.setId(wing.getId());
                    res.setWingName(wing.getWingName());
                    res.setDescription(wing.getDescription());
                    res.setTotal_flats(wing.getTotalFlats());
                    res.setTotal_floors(wing.getTotalFloors());
                    res.setActive(wing.getActive());
                    res.setSociety(wing.getSociety());
                    return res;
                })
                .toList();
    }

    public WingResponse getById(Long id) {
        Wing wing = wingRepository.findById(id)
                .orElseThrow(() -> 
                    new ResourceNotFoundException("Wing not found with id: " + id));
        WingResponse res = new WingResponse();
        res.setId(wing.getId());
        res.setWingName(wing.getWingName());
        res.setDescription(wing.getDescription());
        res.setTotal_floors(wing.getTotalFloors());
        res.setTotal_flats(wing.getTotalFlats());
        res.setActive(wing.getActive());
        res.setSociety(wing.getSociety());
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

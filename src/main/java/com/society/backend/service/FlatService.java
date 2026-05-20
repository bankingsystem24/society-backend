package com.society.backend.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.society.backend.dto.FlatResponse;
import com.society.backend.entity.Flat;
import com.society.backend.exception.ResourceNotFoundException;
import com.society.backend.repository.FlatRepository;

@Service
public class FlatService {

    @Autowired
    private FlatRepository flatRepository;

    public Flat save(Flat flat) {
        return flatRepository.save(flat);
    }

public List<FlatResponse> getAll() {

    List<Flat> flats = flatRepository.findAll();

    return flats.stream().map(flat -> {

        FlatResponse res = new FlatResponse();

        res.setId(flat.getId());
        res.setFlatNo(flat.getFlatNo());
        res.setFloorNo(flat.getFloorNo());
        res.setAreaSqFt(flat.getAreaSqFt());
        res.setBedrooms(flat.getBedrooms());
        res.setMaintenanceAmount(flat.getMaintenanceAmount());
        res.setStatus(flat.getStatus());

        if (flat.getSociety() != null) {
            res.setSocietyId(flat.getSociety().getId());
            res.setSocietyName(flat.getSociety().getSocietyName());
        }

        return res;

    }).toList();
}
    
public FlatResponse getById(Long id) {

    Flat flat = flatRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Flat not found"));

    FlatResponse res = new FlatResponse();

    res.setId(flat.getId());
    res.setFlatNo(flat.getFlatNo());
    res.setFloorNo(flat.getFloorNo());
    res.setAreaSqFt(flat.getAreaSqFt());
    res.setBedrooms(flat.getBedrooms());
    res.setMaintenanceAmount(flat.getMaintenanceAmount());
    res.setStatus(flat.getStatus());

    if (flat.getSociety() != null) {
        res.setSocietyId(flat.getSociety().getId());
        res.setSocietyName(flat.getSociety().getSocietyName());
    }

    return res;
}

    public Flat update(Long id, Flat flat) {

        Flat existing = flatRepository.findById(id).orElse(null);

        if (existing != null) {

            existing.setFlatNo(flat.getFlatNo());
            existing.setWing(flat.getWing());
            existing.setSociety(flat.getSociety());

            return flatRepository.save(existing);
        }

        return null;
    }

    public void delete(Long id) {
        flatRepository.deleteById(id);
    }

}

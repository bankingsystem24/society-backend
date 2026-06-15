package com.society.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.society.backend.dto.FlatResponse;
import com.society.backend.dto.WingRequest;
import com.society.backend.dto.WingResponse;
import com.society.backend.entity.Flat;
import com.society.backend.entity.Society;
import com.society.backend.entity.Wing;
import com.society.backend.exception.ResourceNotFoundException;
import com.society.backend.repository.FlatRepository;
import com.society.backend.repository.SocietyRepository;
import com.society.backend.repository.WingRepository;

@Service
public class WingService {

    @Autowired
    private WingRepository wingRepository;

    @Autowired
    private SocietyRepository societyRepository;

    @Autowired
    private FlatRepository flatRepository;

    // CREATE
    public WingResponse createWing(WingRequest req) {

        Wing wing = new Wing();
        wing.setWingName(req.getWingName());
        wing.setDescription(req.getDescription());
        wing.setActive(req.getActive());
        wing.setTotalFlats(req.getTotal_flats());
        wing.setTotalFloors(req.getTotal_floors());

        if (req.getSociety() != null && req.getSociety().getId() != null) {
            Society s = societyRepository.findById(req.getSociety().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Society not found"));
            wing.setSociety(s);
        }

        Wing saved = wingRepository.save(wing);
        return toResponse(saved);
    }

    // GET ALL
    public List<WingResponse> getAll() {
        return wingRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // GET BY ID
    public WingResponse getById(Long id) {
        Wing wing = wingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wing not found with id: " + id));

        return toResponse(wing);
    }

    public List<WingResponse> getBySocietyId(Long societyId) {
        return wingRepository.findBySocietyId(societyId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // UPDATE
    public WingResponse update(Long id, WingRequest req) {

        Wing existing = wingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Wing not found with id: " + id));

        existing.setWingName(req.getWingName());
        existing.setDescription(req.getDescription());
        existing.setActive(req.getActive());
        existing.setTotalFlats(req.getTotal_flats());
        existing.setTotalFloors(req.getTotal_floors());

        // Society
        if (req.getSociety() != null &&
                req.getSociety().getId() != null) {

            Society society = societyRepository
                    .findById(req.getSociety().getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Society not found"));

            existing.setSociety(society);
        }

        Wing updated = wingRepository.save(existing);

        return toResponse(updated);
    }

    
    // DELETE
    public void delete(Long id) {
        if (!wingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Wing not found with id: " + id);
        }
        wingRepository.deleteById(id);
    }

    // ===================== MAPPER =====================
    private WingResponse toResponse(Wing wing) {

        WingResponse res = new WingResponse();
        res.setId(wing.getId());
        res.setWingName(wing.getWingName());
        res.setDescription(wing.getDescription());
        res.setActive(wing.getActive());
        res.setTotal_flats(wing.getTotalFlats());
        res.setTotal_floors(wing.getTotalFloors());

        if (wing.getSociety() != null) {
            Society s = new Society();
            s.setId(wing.getSociety().getId());
            s.setSocietyName(wing.getSociety().getSocietyName());
            res.setSociety(s);
        }

        return res;
    }

    // FLATS BY SOCIETY (kept as-is but safe)
    public List<FlatResponse> getBySocietyIdFlat(Long societyId,Long financialYearId) {

        List<Flat> flats = flatRepository.findBySociety_Id(societyId);

        return flats.stream().map(flat -> {

            FlatResponse res = new FlatResponse();

            res.setId(flat.getId());
            res.setFlatNo(flat.getFlatNo());
            res.setFloorNo(flat.getFloorNo());
            res.setAreaSqFt(flat.getAreaSqFt());
            res.setBedrooms(flat.getBedrooms());
            res.setMaintenanceAmount(flat.getMaintenanceAmount());
            res.setStatus(flat.getStatus());

        // SOCIETY
        if (flat.getSociety() != null) {

            res.setSocietyId(flat.getSociety().getId());
            res.setSocietyName(flat.getSociety().getSocietyName());
        }

        // WING
        if (flat.getWing() != null) {

            res.setWingId(flat.getWing().getId());
            res.setWingName(flat.getWing().getWingName());
        }

        // OWNER
        if (flat.getOwner() != null) {

            res.setOwnerId(flat.getOwner().getId());
            res.setOwnerName(flat.getOwner().getName());
        }

            return res;

        }).toList();
    }
}
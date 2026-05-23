package com.society.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.society.backend.dto.FlatRequest;
import com.society.backend.dto.FlatResponse;
import com.society.backend.entity.Flat;
import com.society.backend.entity.Member;
import com.society.backend.entity.Society;
import com.society.backend.entity.Wing;
import com.society.backend.exception.ResourceNotFoundException;
import com.society.backend.repository.FlatRepository;
import com.society.backend.repository.MemberRepository;
import com.society.backend.repository.SocietyRepository;
import com.society.backend.repository.WingRepository;

@Service
public class FlatService {

    @Autowired
    private FlatRepository flatRepository;

    @Autowired
    private SocietyRepository societyRepository;

    @Autowired
    private WingRepository wingRepository;

    @Autowired
    private MemberRepository memberRepository;

    // =========================
    // CREATE FLAT
    // =========================
    public FlatResponse createFlat(FlatRequest req) {

        Flat flat = new Flat();

        mapRequestToEntity(req, flat);

        Flat saved = flatRepository.save(flat);

        return toResponse(saved);
    }

    // =========================
    // GET ALL FLATS
    // =========================
    public List<FlatResponse> getAll(Long societyId) {

        List<Flat> flats;

        if (societyId != null) {
            flats = flatRepository.findBySociety_Id(societyId);
        } else {
            flats = flatRepository.findAll();
        }

        return flats.stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================
    // GET FLAT BY ID
    // =========================
    public FlatResponse getById(Long id) {

        Flat flat = flatRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Flat not found"));

        return toResponse(flat);
    }

    // =========================
    // UPDATE FLAT
    // =========================
    public FlatResponse update(Long id, FlatRequest req) {

        Flat existing = flatRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Flat not found"));

        mapRequestToEntity(req, existing);

        Flat updated = flatRepository.save(existing);

        return toResponse(updated);
    }

    // =========================
    // UPDATE STATUS
    // =========================
    public void updateStatus(Long id, Boolean active) {

        Flat flat = flatRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Flat not found"));

        flat.setActive(active);

        flatRepository.save(flat);
    }

    // =========================
    // DELETE FLAT
    // =========================
    public void delete(Long id) {

        Flat flat = flatRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Flat not found"));

        flatRepository.delete(flat);
    }

    // =========================
    // MAP REQUEST → ENTITY
    // =========================
    private void mapRequestToEntity(FlatRequest req, Flat flat) {

        flat.setFlatNo(req.getFlatNo());
        flat.setFloorNo(req.getFloorNo());
        flat.setAreaSqFt(req.getAreaSqFt());
        flat.setBedrooms(req.getBedrooms());
        flat.setMaintenanceAmount(req.getMaintenanceAmount());
        flat.setStatus(req.getStatus());

        flat.setActive(
                req.getActive() != null ? req.getActive() : true
        );

        // =========================
        // SOCIETY
        // =========================
        if (req.getSociety() != null &&
                req.getSociety().getId() != null) {

            Society society = societyRepository
                    .findById(req.getSociety().getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Society not found"));

            flat.setSociety(society);

        } else {
            flat.setSociety(null);
        }

        // =========================
        // WING
        // =========================
        if (req.getWing() != null &&
                req.getWing().getId() != null) {

            Wing wing = wingRepository
                    .findById(req.getWing().getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Wing not found"));

            flat.setWing(wing);

        } else {
            flat.setWing(null);
        }

        // =========================
        // OWNER
        // =========================
        if (req.getOwner() != null &&
                req.getOwner().getId() != null) {

            Member owner = memberRepository
                    .findById(req.getOwner().getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Owner not found"));

            flat.setOwner(owner);

        } else {
            flat.setOwner(null);
        }
    }

    // =========================
    // MAP ENTITY → RESPONSE
    // =========================
    private FlatResponse toResponse(Flat flat) {

        FlatResponse res = new FlatResponse();

        res.setId(flat.getId());
        res.setFlatNo(flat.getFlatNo());
        res.setFloorNo(flat.getFloorNo());
        res.setAreaSqFt(flat.getAreaSqFt());
        res.setBedrooms(flat.getBedrooms());
        res.setMaintenanceAmount(flat.getMaintenanceAmount());
        res.setStatus(flat.getStatus());
        res.setActive(flat.getActive());

        // =========================
        // SOCIETY
        // =========================
        if (flat.getSociety() != null) {

            res.setSocietyId(flat.getSociety().getId());
            res.setSocietyName(flat.getSociety().getSocietyName());
        }

        // =========================
        // WING
        // =========================
        if (flat.getWing() != null) {

            res.setWingId(flat.getWing().getId());
            res.setWingName(flat.getWing().getWingName());
        }

        // =========================
        // OWNER
        // =========================
        if (flat.getOwner() != null) {

            res.setOwnerId(flat.getOwner().getId());
            res.setOwnerName(flat.getOwner().getName());
        }

        return res;
    }
}
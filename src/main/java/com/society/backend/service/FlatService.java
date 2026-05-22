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


    public FlatResponse createFlat(FlatRequest req) {

        Flat flat = new Flat();

        flat.setFlatNo(req.getFlatNo());
        flat.setFloorNo(req.getFloorNo());
        flat.setActive(req.getActive());
        flat.setAreaSqFt(req.getAreaSqFt());
        flat.setBedrooms(req.getBedrooms());
        flat.setMaintenanceAmount(req.getMaintenanceAmount());
        flat.setStatus(req.getStatus());

        // Society
        if (req.getSociety() != null && req.getSociety().getId() != null) {
            Society s = societyRepository.findById(req.getSociety().getId())
                    .orElseThrow(() -> new RuntimeException("Society not found"));
            flat.setSociety(s);
        }

        // Wing
        if (req.getWing() != null && req.getWing().getId() != null) {
            Wing w = wingRepository.findById(req.getWing().getId())
                    .orElseThrow(() -> new RuntimeException("Wing not found"));
            flat.setWing(w);
        }

        // Owner
        if (req.getOwner() != null && req.getOwner().getId() != null) {
            Member m = memberRepository.findById(req.getOwner().getId())
                    .orElseThrow(() -> new RuntimeException("Owner not found"));
            flat.setOwner(m);
        }

        Flat saved = flatRepository.save(flat);

        return toResponse(saved);
    }

    private FlatResponse toResponse(Flat flat){
        FlatResponse res = new FlatResponse();
        res.setId(flat.getId());
        res.setAreaSqFt(flat.getAreaSqFt());
        res.setBedrooms(flat.getBedrooms());
        res.setFlatNo(flat.getFlatNo());
        res.setFloorNo(flat.getFloorNo());
        res.setMaintenanceAmount(flat.getMaintenanceAmount());

        if(flat.getSociety() != null) {
            res.setSociety(flat.getSociety());
        }

        if(flat.getWing() != null) {
            res.setWing(flat.getWing());
        }

        if(flat.getOwner() != null) {
            res.setOwner(flat.getOwner());
        }

        return res;
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

            // Society
            if (flat.getSociety() != null) {
                Society s = new Society();
                s.setId(flat.getSociety().getId());
                s.setSocietyName(flat.getSociety().getSocietyName());
                res.setSociety(s);
            }

            // Wing
            if (flat.getWing() != null) {
                Wing w = new Wing();
                w.setId(flat.getWing().getId());
                w.setWingName(flat.getWing().getWingName());
                res.setWing(w);
            }

            // Owner
            if (flat.getOwner() != null) {
                Member m = new Member();
                m.setId(flat.getOwner().getId());
                m.setName(flat.getOwner().getName());
                res.setOwner(m);
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

        // Society
        if (flat.getSociety() != null) {
            Society s = new Society();
            s.setId(flat.getSociety().getId());
            s.setSocietyName(flat.getSociety().getSocietyName());
            res.setSociety(s);
        }

        // Wing
        if (flat.getWing() != null) {
            Wing w = new Wing();
            w.setId(flat.getWing().getId());
            w.setWingName(flat.getWing().getWingName());
            res.setWing(w);
        }

        // Owner
        if (flat.getOwner() != null) {
            Member m = new Member();
            m.setId(flat.getOwner().getId());
            m.setName(flat.getOwner().getName());
            res.setOwner(m);
        }

        return res;
    }

    public Flat update(Long id, Flat req) {

        Flat existing = flatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flat not found"));

        existing.setFlatNo(req.getFlatNo());
        existing.setFloorNo(req.getFloorNo());
        existing.setAreaSqFt(req.getAreaSqFt());
        existing.setBedrooms(req.getBedrooms());
        existing.setMaintenanceAmount(req.getMaintenanceAmount());
        existing.setStatus(req.getStatus());

        if (req.getSociety() != null) {
            Society s = societyRepository.findById(req.getSociety().getId())
                    .orElseThrow(() -> new RuntimeException("Society not found"));
            existing.setSociety(s);
        }

        if (req.getWing() != null) {
            Wing w = wingRepository.findById(req.getWing().getId())
                    .orElseThrow(() -> new RuntimeException("Wing not found"));
            existing.setWing(w);
        }

        if (req.getOwner() != null) {
            Member m = memberRepository.findById(req.getOwner().getId())
                    .orElseThrow(() -> new RuntimeException("Owner not found"));
            existing.setOwner(m);
        }

        return flatRepository.save(existing);
    }

    public List<FlatResponse> getBySocietyId(Long societyId) {

        List<Flat> flats = flatRepository.findAll()
                .stream()
                .filter(f -> f.getSociety() != null
                        && f.getSociety().getId().equals(societyId))
                .toList();

                return flats.stream().map(flat -> {

            FlatResponse res = new FlatResponse();

            res.setId(flat.getId());
            res.setFlatNo(flat.getFlatNo());
            res.setFloorNo(flat.getFloorNo());
            res.setAreaSqFt(flat.getAreaSqFt());
            res.setBedrooms(flat.getBedrooms());
            res.setMaintenanceAmount(flat.getMaintenanceAmount());
            res.setStatus(flat.getStatus());

            // Society
            if (flat.getSociety() != null) {
                Society s = new Society();
                s.setId(flat.getSociety().getId());
                s.setSocietyName(flat.getSociety().getSocietyName());
                res.setSociety(s);
            }

            // Wing
            if (flat.getWing() != null) {
                Wing w = new Wing();
                w.setId(flat.getWing().getId());
                w.setWingName(flat.getWing().getWingName());
                res.setWing(w);
            }

            // Owner
            if (flat.getOwner() != null) {
                Member m = new Member();
                m.setId(flat.getOwner().getId());
                m.setName(flat.getOwner().getName());
                res.setOwner(m);
            }

            return res;

        }).toList();
    }

    
    public void delete(Long id) {
        flatRepository.deleteById(id);
    }

    
}

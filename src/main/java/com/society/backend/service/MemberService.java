package com.society.backend.service;

import com.society.backend.dto.MemberRequest;
import com.society.backend.dto.MemberResponse;
import com.society.backend.entity.Flat;
import com.society.backend.entity.Member;
import com.society.backend.entity.Society;
import com.society.backend.exception.ResourceNotFoundException;
import com.society.backend.repository.FlatRepository;
import com.society.backend.repository.MemberRepository;
import com.society.backend.repository.SocietyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberRepository repository;

    @Autowired
    private SocietyRepository societyRepository;
    @Autowired
    private FlatRepository flatRepository;
    
    // Save Member
    public Member save(Member member) {
        return repository.save(member);
    }

    // Get All Members
    public List<MemberResponse> getAll() {
        return repository.findAll().stream().map(member -> {

            MemberResponse res = new MemberResponse();

            res.setId(member.getId());
            res.setName(member.getName());
            res.setEmail(member.getEmail());
            res.setMobile(member.getMobile());
            res.setAddress(member.getAddress());

            if (member.getFlat() != null) {
                res.setFlatId(member.getFlat().getId());
                res.setFlatNo(member.getFlat().getFlatNo());
            }

            return res;

        }).toList();
    }

public MemberResponse createMember(MemberRequest req) {

    Member m = new Member();

    m.setName(req.getName());
    m.setEmail(req.getEmail());
    m.setMobile(req.getMobile());
    m.setAddress(req.getAddress());
    m.setGender(req.getGender());
    m.setOccupation(req.getOccupation());
    m.setMemberType(req.getMemberType());
    m.setActive(true);

    // SOCIETY FIX
    if (req.getSocietyId() != null) {
        Society s = societyRepository.findById(req.getSocietyId())
                .orElseThrow(() -> new RuntimeException("Society not found"));
        m.setSociety(s);
    }

    // FLAT FIX
    if (req.getFlatId() != null) {
        Flat f = flatRepository.findById(req.getFlatId())
                .orElseThrow(() -> new RuntimeException("Flat not found"));
        m.setFlat(f);
    }

    Member saved = repository.save(m);
    return mapToResponse(saved);
}

    private MemberResponse mapToResponse(Member m) {
        MemberResponse r = new MemberResponse();

        r.setId(m.getId());
        r.setName(m.getName());
        r.setEmail(m.getEmail());
        r.setMobile(m.getMobile());
        r.setAddress(m.getAddress());
        r.setGender(m.getGender());
        r.setOccupation(m.getOccupation());
        r.setMemberType(m.getMemberType());
        r.setActive(m.getActive());

        if (m.getFlat() != null) {
            r.setFlatId(m.getFlat().getId());
            r.setFlatNo(m.getFlat().getFlatNo());
        }

        return r;
    }
    // Get Member By ID
    public MemberResponse getById(Long id) {
        Member member = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        MemberResponse res = new MemberResponse();

        res.setId(member.getId());
        res.setName(member.getName());
        res.setEmail(member.getEmail());
        res.setMobile(member.getMobile());
        res.setAddress(member.getAddress());

        if (member.getFlat() != null) {
            res.setFlatId(member.getFlat().getId());
            res.setFlatNo(member.getFlat().getFlatNo());
        }

        return res;
    }

    // Update Member
    public Member update(Long id, Member member) {

        Member existing = repository.findById(id).orElse(null);

        if (existing != null) {

            existing.setName(member.getName());
            existing.setEmail(member.getEmail());
            existing.setMobile(member.getMobile());
            existing.setAddress(member.getAddress());

            return repository.save(existing);
        }

        return null;
    }

    // Delete Member
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
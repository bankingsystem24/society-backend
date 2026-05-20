package com.society.backend.service;

import com.society.backend.dto.MemberResponse;
import com.society.backend.entity.Member;
import com.society.backend.exception.ResourceNotFoundException;
import com.society.backend.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberRepository repository;

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
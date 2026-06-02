package com.society.backend.service;

import com.society.backend.dto.MemberRequest;
import com.society.backend.dto.MemberResponse;
import com.society.backend.entity.Member;
import com.society.backend.entity.Society;
import com.society.backend.exception.ResourceNotFoundException;
import com.society.backend.repository.MemberRepository;
import com.society.backend.repository.SocietyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SocietyRepository societyRepository;


    // =========================
    // CREATE MEMBER
    // =========================

    public MemberResponse createMember(MemberRequest req) {

        Member member = new Member();

        mapRequestToEntity(req, member);

        Member saved = memberRepository.save(member);

        return toResponse(saved);
    }

    // =========================
    // GET ALL MEMBERS
    // =========================

    public List<MemberResponse> getAll(Long societyId) {

        List<Member> members;

        if (societyId != null) {
            members = memberRepository.findBySociety_Id(societyId);
        } else {
            members = memberRepository.findAll();
        }

        return members.stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================
    // GET BY ID
    // =========================

    public MemberResponse getById(Long id) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Member not found"));

        return toResponse(member);
    }

    // =========================
    // UPDATE MEMBER
    // =========================

    public MemberResponse update(Long id, MemberRequest req) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Member not found"));

        mapRequestToEntity(req, member);

        Member updated = memberRepository.save(member);

        return toResponse(updated);
    }

    // =========================
    // UPDATE STATUS
    // =========================

    public void updateStatus(Long id, Boolean active) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Member not found"));

        member.setActive(active);

        memberRepository.save(member);
    }

    // =========================
    // DELETE
    // =========================

    public void delete(Long id) {
        memberRepository.deleteById(id);
    }

    // =========================
    // REQUEST → ENTITY
    // =========================

    private void mapRequestToEntity(MemberRequest req, Member member) {

        member.setName(req.getName());
        member.setEmail(req.getEmail());
        member.setMobile(req.getMobile());
        member.setAddress(req.getAddress());
        member.setGender(req.getGender());
        member.setOccupation(req.getOccupation());
        member.setMemberType(req.getMemberType());

        if (req.getActive() != null) {
            member.setActive(req.getActive());
        }

        // =========================
        // SOCIETY
        // =========================

        if (req.getSocietyId() != null) {

            Society society = societyRepository.findById(req.getSocietyId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Society not found"));

            member.setSociety(society);

        } else {
            member.setSociety(null);
        }

        // =========================
        // FLAT
        // =========================


    }

    // =========================
    // ENTITY → RESPONSE
    // =========================

    private MemberResponse toResponse(Member member) {

        MemberResponse res = new MemberResponse();

        res.setId(member.getId());
        res.setName(member.getName());
        res.setEmail(member.getEmail());
        res.setMobile(member.getMobile());
        res.setAddress(member.getAddress());
        res.setGender(member.getGender());
        res.setOccupation(member.getOccupation());
        res.setMemberType(member.getMemberType());
        res.setActive(member.getActive());

        // SOCIETY
        if (member.getSociety() != null) {

            res.setSocietyId(member.getSociety().getId());
            res.setSocietyName(member.getSociety().getSocietyName());
        }

        // FLAT
        // if (member.getFlat() != null) {

        //     res.setFlatId(member.getFlat().getId());
        //     res.setFlatNo(member.getFlat().getFlatNo());
        // }

        return res;
    }
}
package com.society.backend.controller;

import com.society.backend.dto.MemberRequest;
import com.society.backend.dto.MemberResponse;
import com.society.backend.entity.Member;
import com.society.backend.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@CrossOrigin("*")
public class MemberController {

    @Autowired
    private MemberService service;

    // Create Member
    @PostMapping
    public ResponseEntity<MemberResponse> create(@RequestBody MemberRequest request) {
        return ResponseEntity.ok(service.createMember(request));
    }
    
    // Get All Members
    @GetMapping
    public List<MemberResponse> getAll(@RequestParam(required = false) Long societyId) {
        return service.getAll(societyId);
    }

    // Get Member By ID
    @GetMapping("/{id}")
    public MemberResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/update-status")
    public ResponseEntity<?> updateStatus(
            @RequestParam Long id,
            @RequestParam Boolean active) {

        service.updateStatus(id, active);

        return ResponseEntity.ok("Status updated");
    }

    // Update Member
    @PutMapping("/{id}")
    public Member update(@PathVariable Long id, @RequestBody Member member) {
        return service.update(id, member);
    }

    // Delete Member
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Member deleted successfully";
    }
}
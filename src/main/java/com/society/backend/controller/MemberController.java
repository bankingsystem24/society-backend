package com.society.backend.controller;

import com.society.backend.dto.MemberRequest;
import com.society.backend.dto.MemberResponse;
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
    private MemberService memberService;

    // =========================
    // CREATE MEMBER
    // =========================
    @PostMapping
    public ResponseEntity<MemberResponse> create(@RequestBody MemberRequest request) {
        return ResponseEntity.ok(memberService.createMember(request));
    }

    // =========================
    // GET ALL MEMBERS
    // =========================
    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAll(
            @RequestParam(required = false) Long societyId) {

        return ResponseEntity.ok(memberService.getAll(societyId));
    }

    // =========================
    // GET BY ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getById(id));
    }

    // =========================
    // UPDATE MEMBER
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<MemberResponse> update(
            @PathVariable Long id,
            @RequestBody MemberRequest request) {

        return ResponseEntity.ok(memberService.update(id, request));
    }

    // =========================
    // UPDATE STATUS
    // =========================
    @PutMapping("/update-status")
    public ResponseEntity<String> updateStatus(
            @RequestParam Long id,
            @RequestParam Boolean active) {

        memberService.updateStatus(id, active);
        return ResponseEntity.ok("Status updated successfully");
    }

    // =========================
    // DELETE MEMBER
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        memberService.delete(id);
        return ResponseEntity.ok("Member deleted successfully");
    }
}
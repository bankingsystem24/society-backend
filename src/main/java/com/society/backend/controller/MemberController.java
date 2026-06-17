package com.society.backend.controller;

import com.society.backend.dto.MemberRequest;
import com.society.backend.dto.MemberResponse;
import com.society.backend.dto.SinkingFundResponse;
import com.society.backend.entity.Billing;
import com.society.backend.entity.Flat;
import com.society.backend.entity.SinkingFund;
import com.society.backend.gl.service.BillingService;
import com.society.backend.service.FlatService;
import com.society.backend.service.MemberService;
import com.society.backend.service.SinkingFundService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MemberController {

    private final MemberService memberService;
    private final FlatService flatService;
    private final BillingService billingService;
    private final SinkingFundService sinkingFundService;

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

    @GetMapping("/flats")
    public List<Flat> getMemberFlats(
            @RequestParam Long societyId,
            @RequestParam Long memberId) {
        return flatService.getFlatsForMember(societyId, memberId);
    }

    @PostMapping("/bills")
    public List<Billing> getBills(@RequestBody Map<String, Object> req) {

        List<?> rawList = (List<?>) req.get("flatIds");

        List<Long> flatIds = rawList.stream()
                .map(id -> Long.valueOf(id.toString()))
                .toList();

        Long societyId = Long.valueOf(req.get("societyId").toString());
        Long financialYearId = Long.valueOf(req.get("financialYearId").toString());

        return billingService.getBillsByFlatIds(flatIds, societyId, financialYearId);

    }

    @PostMapping("/sinking-funds")
    public List<SinkingFundResponse> getSinkingFunds(
            @RequestBody Map<String, Object> req) {

        List<?> rawList = (List<?>) req.get("flatIds");

        List<Long> flatIds = rawList.stream()
                .map(id -> Long.valueOf(id.toString()))
                .toList();

        Long societyId = Long.valueOf(req.get("societyId").toString());
        Long financialYearId = Long.valueOf(req.get("financialYearId").toString());

        return sinkingFundService.getSinkingFundsByFlatIds(flatIds,societyId,financialYearId);
    }

}
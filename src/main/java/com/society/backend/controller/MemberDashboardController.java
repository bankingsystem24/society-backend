package com.society.backend.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.society.backend.service.MemberDashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MemberDashboardController {

    private final MemberDashboardService memberDashboardService;

    @PostMapping("/dashboard")
    public Map<String, Object> getDashboard(@RequestBody Map<String, Object> req) {
        Long memberId = Long.parseLong(req.get("memberId").toString());
        Long financialYearId = Long.parseLong(req.get("financialYearId").toString());
        return memberDashboardService.getDashboard(memberId,financialYearId);
    }
}
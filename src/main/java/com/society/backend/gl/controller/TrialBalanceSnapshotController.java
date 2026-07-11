package com.society.backend.gl.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.society.backend.gl.dto.SaveTrialBalanceRequest;
import com.society.backend.gl.dto.TrialBalanceDetailDTO;
import com.society.backend.gl.service.TrialBalanceSnapshotService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/trial-balance-snapshot")
@RequiredArgsConstructor
public class TrialBalanceSnapshotController {

    private final TrialBalanceSnapshotService snapshotService;


    @PostMapping("/save")
    public ResponseEntity<?> saveSnapshot(
            @RequestBody SaveTrialBalanceRequest request) {

        snapshotService.saveSnapshot(request);

        return ResponseEntity.ok(
            "Trial Balance Snapshot saved successfully"
        );
    }

    @GetMapping("/view")
    public ResponseEntity<List<TrialBalanceDetailDTO>> viewSnapshot(
            @RequestParam Long financialYearId) {

        return ResponseEntity.ok(
                snapshotService.getSnapshot(financialYearId));
    }
}
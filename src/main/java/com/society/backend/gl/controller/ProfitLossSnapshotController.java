package com.society.backend.gl.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.society.backend.gl.dto.ProfitLossSnapshotRequest;
import com.society.backend.gl.dto.ProfitLossSnapshotResponse;
import com.society.backend.gl.service.ProfitLossSnapshotService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/profit-loss-snapshot")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProfitLossSnapshotController {

    private final ProfitLossSnapshotService profitLossSnapshotService;

    @PostMapping("/save")
    public ResponseEntity<String> newSaveSnapshot(
            @RequestBody ProfitLossSnapshotRequest request) {

        profitLossSnapshotService.newSaveSnapshot(request);

        return ResponseEntity.ok("Snapshot saved successfully.");
    }

    @GetMapping("/{financialYearId}")
    public ResponseEntity<ProfitLossSnapshotResponse> getSnapshot(
            @PathVariable Long financialYearId) {

        ProfitLossSnapshotResponse response =
                profitLossSnapshotService.getSnapshot(financialYearId);

        return ResponseEntity.ok(response);
    }

    /**
     * Get All Profit & Loss Snapshots
     */
    @GetMapping
    public ResponseEntity<List<ProfitLossSnapshotResponse>> getAllSnapshots() {

        List<ProfitLossSnapshotResponse> response =
                profitLossSnapshotService.getAllSnapshots();

        return ResponseEntity.ok(response);
    }

    /**
     * Delete Profit & Loss Snapshot
     */
    @DeleteMapping("/{financialYearId}")
    public ResponseEntity<String> deleteSnapshot(
            @PathVariable Long financialYearId) {

        profitLossSnapshotService.deleteSnapshot(financialYearId);

        return ResponseEntity.ok("Profit & Loss Snapshot deleted successfully.");
    }
}
package com.society.backend.gl.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.society.backend.gl.dto.BalanceSheetDTO;
import com.society.backend.gl.dto.SaveBalanceSheetRequest;
import com.society.backend.gl.service.BalanceSheetSnapshotService;

@RestController
@RequestMapping("/api/balance-sheet-snapshot")
@CrossOrigin(origins = "*")
public class BalanceSheetSnapshotController {

    private final BalanceSheetSnapshotService balanceSheetSnapshotService;

    public BalanceSheetSnapshotController(
            BalanceSheetSnapshotService balanceSheetSnapshotService) {
        this.balanceSheetSnapshotService = balanceSheetSnapshotService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveSnapshot(
            @RequestBody SaveBalanceSheetRequest request) {

        balanceSheetSnapshotService.saveSnapshot(request);

        return ResponseEntity.ok("Balance Sheet Snapshot saved successfully");
    }

    @GetMapping("/{financialYearId}")
    public ResponseEntity<BalanceSheetDTO> getBalanceSheet(
            @PathVariable Long financialYearId) {

        BalanceSheetDTO dto =
                balanceSheetSnapshotService.getBalanceSheet(financialYearId);

        return ResponseEntity.ok(dto);
    }
}
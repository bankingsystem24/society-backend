package com.society.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.society.backend.dto.FlatRequest;
import com.society.backend.dto.FlatResponse;
import com.society.backend.service.FlatService;

@RestController
@RequestMapping("/api/flats")
@CrossOrigin("*")
public class FlatController {

    private final FlatService service;

    public FlatController(FlatService service){
        this.service = service;
    }

    // =========================
    // CREATE FLAT
    // =========================
    @PostMapping
    public ResponseEntity<FlatResponse> create(
            @RequestBody FlatRequest request) {

        return ResponseEntity.ok(service.createFlat(request));
    }

    // =========================
    // GET ALL FLATS
    // =========================
    @GetMapping
    public ResponseEntity<List<FlatResponse>> getAll(
            @RequestParam(required = false) Long societyId, Long financialYearId) {

        return ResponseEntity.ok(service.getAll(societyId,financialYearId));
    }

    // =========================
    // GET FLAT BY ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<FlatResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getById(id));
    }

    // =========================
    // UPDATE FLAT
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<FlatResponse> update(
            @PathVariable Long id,
            @RequestBody FlatRequest request) {

        return ResponseEntity.ok(service.update(id, request));
    }

    // =========================
    // UPDATE STATUS
    // =========================
    @PutMapping("/update-status")
    public ResponseEntity<String> updateStatus(
            @RequestParam Long id,
            @RequestParam Boolean active) {

        service.updateStatus(id, active);

        return ResponseEntity.ok("Flat status updated");
    }

    // =========================
    // DELETE FLAT
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok("Flat deleted successfully");
    }
}
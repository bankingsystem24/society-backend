package com.society.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.society.backend.dto.WingResponse;
import com.society.backend.entity.Wing;
import com.society.backend.service.WingService;

@RestController
@RequestMapping("/api/wings")
@CrossOrigin("*")
public class WingController {

    @Autowired
    private WingService service;

    // =========================
    // Create Wing
    // =========================

    @PostMapping
    public ResponseEntity<Wing> create(@RequestBody Wing wing) {

        return ResponseEntity.ok(service.save(wing));
    }

    // =========================
    // Get All Wings
    // =========================

    @GetMapping
    public ResponseEntity<List<WingResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // =========================
    // Get Wing By ID
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<WingResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // =========================
    // Update Wing
    // =========================

    @PutMapping("/{id}")
    public ResponseEntity<Wing> update(@PathVariable Long id,
                                       @RequestBody Wing wing) {

        return ResponseEntity.ok(service.update(id, wing));
    }

    // =========================
    // Delete Wing
    // =========================

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok("Wing deleted successfully");
    }
}
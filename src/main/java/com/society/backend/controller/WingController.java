package com.society.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.society.backend.dto.WingRequest;
import com.society.backend.dto.WingResponse;
import com.society.backend.service.WingService;

@RestController
@RequestMapping("/api/wings")
@CrossOrigin("*")
public class WingController {

    @Autowired
    private WingService service;

    // CREATE
    @PostMapping
    public ResponseEntity<WingResponse> create(@RequestBody WingRequest request) {
        return ResponseEntity.ok(service.createWing(request));
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<WingResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<WingResponse>> getAll(
            @RequestParam(required = false) Long societyId) {

        if (societyId != null) {
            return ResponseEntity.ok(service.getBySocietyId(societyId));
        }

        return ResponseEntity.ok(service.getAll());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<WingResponse> update(
            @PathVariable Long id,
            @RequestBody com.society.backend.entity.Wing wing) {
        return ResponseEntity.ok(service.update(id, wing));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Wing deleted successfully");
    }
}
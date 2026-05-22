package com.society.backend.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.society.backend.dto.WingRequest;
import com.society.backend.dto.WingResponse;
import com.society.backend.entity.Wing;
import com.society.backend.service.WingService;

@RestController
@RequestMapping("/api/wings")
@CrossOrigin("*")
public class WingController {

    @Autowired
    private WingService service;

    @PostMapping
    public ResponseEntity<WingResponse> create(
            @RequestBody WingRequest request) {

        return ResponseEntity.ok(service.createWing(request));
    }

    @GetMapping
    public ResponseEntity<List<WingResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WingResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public Wing update(@PathVariable Long id, @RequestBody Wing wing) {

        return service.update(id, wing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok("Wing deleted successfully");
    }
}
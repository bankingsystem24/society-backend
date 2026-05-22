package com.society.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.society.backend.entity.Flat;
import com.society.backend.service.FlatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.society.backend.dto.FlatRequest;
import com.society.backend.dto.FlatResponse;

@RestController
@RequestMapping("/api/flats")
@CrossOrigin("*")
public class FlatController {
    
    @Autowired
    private FlatService service;
    // Create Flat
    
    @PostMapping
    public ResponseEntity<FlatResponse> create(@RequestBody FlatRequest request) {
        return ResponseEntity.ok(service.createFlat(request));
    }

    
    @GetMapping
    public ResponseEntity<List<FlatResponse>> getAllFlats(
            @RequestParam(required = false) Long societyId) {

        List<FlatResponse> flats;

        if (societyId != null) {
            flats = service.getBySocietyId(societyId);
        } else {
            flats = service.getAll();
        }

        return ResponseEntity.ok(flats);
    }


    @GetMapping("/{id}")
    public FlatResponse getById(@PathVariable Long id) {
        return service.getById(id);    
    }


    @PutMapping("/{id}")
    public ResponseEntity<Flat> update(@PathVariable Long id, @RequestBody Flat entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Flat deleted successfully");
    }

}

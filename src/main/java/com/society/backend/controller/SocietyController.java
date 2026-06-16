package com.society.backend.controller;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.society.backend.entity.Society;
import com.society.backend.service.SocietyService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/societies")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SocietyController {

    private final SocietyService service;

    @PostMapping
    public ResponseEntity<Society> create(@RequestBody Society  society) {
        return ResponseEntity.ok(service.save(society));
    }
    
    @GetMapping
    public ResponseEntity<List<Society>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Society> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
        
    @PutMapping("/{id}")
    public ResponseEntity<Society> update(@PathVariable Long id, @RequestBody Society society) {
        return ResponseEntity.ok(service.update(id, society));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Society deleted successfully");
    }


}

package com.society.backend.gl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.society.backend.gl.dto.GlOpeningBalanceRequest;
import com.society.backend.gl.entity.GlOpeningBalance;
import com.society.backend.gl.service.GlOpeningBalanceService;

@RestController
@RequestMapping("/api/gl/opening-balance")
@CrossOrigin("*")
public class GlOpeningBalanceController {

    @Autowired
    private GlOpeningBalanceService service;

    @GetMapping
    public List<GlOpeningBalance> getAll(@RequestParam Long societyId) {
        return service.getAllOpeningBySociety(societyId);
    }

    @PostMapping("/save")
    public GlOpeningBalance save(
            @RequestBody GlOpeningBalance entity,
            @RequestParam Long societyId
    ) {
        return service.save(entity, societyId);
    }

@PutMapping("/{id}")
public GlOpeningBalance update(
        @PathVariable Long id,
        @RequestBody GlOpeningBalanceRequest request) {

    return service.update(id, request);
}


    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted successfully");
    }

}

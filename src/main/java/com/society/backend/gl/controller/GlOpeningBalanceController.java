package com.society.backend.gl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping
    public GlOpeningBalance create(@RequestBody GlOpeningBalance entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    public GlOpeningBalance update(@PathVariable Long id,
                                   @RequestBody GlOpeningBalance entity) {
        return service.update(id, entity);
    }
}

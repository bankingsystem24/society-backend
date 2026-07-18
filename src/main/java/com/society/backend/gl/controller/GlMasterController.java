package com.society.backend.gl.controller;

import com.society.backend.gl.repository.GlMappingRepository;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.society.backend.gl.entity.GlMapping;
import com.society.backend.gl.entity.GlMaster;
import com.society.backend.gl.service.GlMappingService;
import com.society.backend.gl.service.GlMasterService;

@RestController
@RequestMapping("/api/gl/master")
@CrossOrigin(origins = "*")
public class GlMasterController {

    private final GlMasterService glMasterService;
    private final GlMappingRepository glMappingRepository;
    private final GlMappingService glMappingService;

    public GlMasterController(GlMasterService glMasterService,
            GlMappingRepository glMappingRepository,
            GlMappingService glMappingService) {
        this.glMasterService = glMasterService;
        this.glMappingRepository = glMappingRepository;
        this.glMappingService = glMappingService;
    };

    @GetMapping
    public List<GlMaster> getGlMaster(@RequestParam Long societyId) {
        return glMasterService.getAllBySociety(societyId);
    }

    @GetMapping("/{id}")
    public List<GlMaster> getById(@PathVariable Long id) {
        return glMasterService.getById(id);
    }

    @PostMapping
    public GlMaster create(
            @RequestBody GlMaster glMaster) {

        return glMasterService.save(glMaster);
    }

    @PutMapping("/{glCode}")
    public GlMaster update(
            @PathVariable Integer glCode,
            @RequestParam Long societyId,
            @RequestBody GlMaster glMaster) {

        return glMasterService.update(
                glCode,
                societyId,
                glMaster);
    }

    @DeleteMapping("/{glCode}")
    public void delete(
            @PathVariable Integer glCode,
            @RequestParam Long societyId) {

        glMasterService.delete(
                glCode,
                societyId);
    }

    @PostMapping("/mapping")
    public ResponseEntity<GlMapping> saveMapping(
            @RequestBody GlMapping glMapping) {

        GlMapping saved = glMasterService.save(glMapping);

        return ResponseEntity.ok(saved);
    }

    @GetMapping("/mapping")
    public List<GlMapping> getGlMapping(@RequestParam Long societyId) {
        return glMasterService.getMappingBySociety(societyId);
    }

    @PostMapping("/defaultgl")
    public ResponseEntity<String> createDefaultGL(@RequestBody Long societyId) {
        glMasterService.createDefaultGL(societyId);
        return ResponseEntity.ok("Default GLs created successfully");
    }

    @DeleteMapping("/mapping/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        glMappingRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/mapping/{id}")
    public ResponseEntity<GlMapping> update(
            @PathVariable Long id,
            @RequestBody GlMapping glMapping) {

        return ResponseEntity.ok(glMappingService.update(id, glMapping));
    }

    @GetMapping("/cash-bank")
    public List<GlMaster> getCashBankAccounts(
            @RequestParam Long societyId) {
                
        return glMasterService.getCashBankAccounts(societyId);
    }


}
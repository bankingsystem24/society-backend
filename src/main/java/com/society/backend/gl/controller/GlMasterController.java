package com.society.backend.gl.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.society.backend.gl.entity.GlMapping;
import com.society.backend.gl.entity.GlMaster;
import com.society.backend.gl.service.GlMasterService;

@RestController
@RequestMapping("/api/gl/master")
@CrossOrigin(origins = "*")
public class GlMasterController {

    private final GlMasterService glMasterService;

    public GlMasterController(GlMasterService glMasterService){
        this.glMasterService = glMasterService;
    };

    @GetMapping
    public List<GlMaster> getGlMaster(@RequestParam Long societyId) {
        return glMasterService.getAllBySociety(societyId);
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


}
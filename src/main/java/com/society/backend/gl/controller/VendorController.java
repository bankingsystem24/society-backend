package com.society.backend.gl.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.society.backend.gl.entity.Vendor;
import com.society.backend.gl.service.VendorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vendors")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VendorController {

    private final VendorService vendorService;

    @PostMapping
    public Vendor save(@RequestBody Vendor vendor) {
        return vendorService.save(vendor);
    }

    @GetMapping("/{societyId}")
    public List<Vendor> getBySociety(@PathVariable Long societyId) {
        return vendorService.getBySociety(societyId);
    }

    @GetMapping("/details/{id}")
    public Vendor getById(
            @PathVariable Long id) {
        return vendorService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id) {
        vendorService.delete(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vendor> updateVendor(
            @PathVariable Long id,
            @RequestBody Vendor vendor) {

        Vendor updated = vendorService.updateVendor(id, vendor);
        return ResponseEntity.ok(updated);
    }

}

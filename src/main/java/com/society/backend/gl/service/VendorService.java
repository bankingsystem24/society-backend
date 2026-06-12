package com.society.backend.gl.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.society.backend.gl.entity.Vendor;
import com.society.backend.gl.repository.VendorRepository;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;
    public VendorService(VendorRepository vendorRepository){
        this.vendorRepository = vendorRepository;
    }

    public Vendor save(Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    public List<Vendor> getBySociety(Long societyId) {
        return vendorRepository.findBySocietyIdOrderByVendorNameAsc(societyId);
    }

    public Vendor getById(Long id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
    }

    public void delete(Long id) {
        vendorRepository.deleteById(id);
    }

    
    public Vendor updateVendor(Long id, Vendor vendor) {

        Vendor existing = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found with id: " + id));

        existing.setVendorName(vendor.getVendorName());
        existing.setMobileNo(vendor.getMobileNo());
        existing.setGstNo(vendor.getGstNo());
        existing.setAddress(vendor.getAddress());
        existing.setPayableGlCode(vendor.getPayableGlCode());
        existing.setSocietyId(vendor.getSocietyId());

        return vendorRepository.save(existing);
    }

}

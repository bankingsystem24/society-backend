package com.society.backend.gl.service;

import org.springframework.stereotype.Service;

import com.society.backend.gl.repository.GlMappingRepository;

import lombok.AllArgsConstructor;

import com.society.backend.gl.entity.GlMapping;

@Service
@AllArgsConstructor
public class GlMappingService {

    private final GlMappingRepository glMappingRepository;

        public GlMapping update(Long id, GlMapping request) {

        GlMapping mapping = glMappingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GL Mapping not found"));

        mapping.setDescription(request.getDescription());
        mapping.setGl_receivable(request.getGl_receivable());
        mapping.setGl_credit_account(request.getGl_credit_account());

        return glMappingRepository.save(mapping);
    }


    
}

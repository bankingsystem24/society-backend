package com.society.backend.gl.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.society.backend.gl.entity.GlMaster;
import com.society.backend.gl.repository.GlMasterRepository;

@Service
public class GlMasterService {

    @Autowired
    private GlMasterRepository glMasterRepository;

    public List<GlMaster> getAllBySociety(Long societyId) {
        return glMasterRepository.findBySocietyIdOrderByGlCodeAsc(societyId);
    }

}
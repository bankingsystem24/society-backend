package com.society.backend.gl.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.society.backend.gl.entity.GlMaster;
import com.society.backend.gl.service.GlMasterService;

@RestController
@RequestMapping("/api/gl/master")
@CrossOrigin(origins = "*")
public class GlMasterController {

    @Autowired
    private GlMasterService glMasterService;

    @GetMapping
    public List<GlMaster> getGlMaster(@RequestParam Long societyId) {
        return glMasterService.getAllBySociety(societyId);
    }


}
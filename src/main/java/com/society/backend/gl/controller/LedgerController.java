package com.society.backend.gl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.society.backend.gl.dto.LedgerDTO;
import com.society.backend.gl.service.LedgerService;

@RestController
@RequestMapping("/api/ledger")
@CrossOrigin("*")
public class LedgerController {

    @Autowired
    private LedgerService service;

    @GetMapping("/{societyId}/{glCode}")
    public List<LedgerDTO> getLedger(

            @PathVariable Long societyId,

            @PathVariable Integer glCode

    ) {

        return service.getLedger(societyId, glCode);
    }
}
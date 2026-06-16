package com.society.backend.gl.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.society.backend.gl.dto.LedgerDTO;
import com.society.backend.gl.service.LedgerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ledger")
@CrossOrigin("*")
@RequiredArgsConstructor
public class LedgerController {

    private final LedgerService service;

    @GetMapping("/{societyId}/{glCode}")
    public List<LedgerDTO> getLedger(

            @PathVariable Long societyId,

            @PathVariable Integer glCode

    ) {

        return service.getLedger(societyId, glCode);
    }
}
package com.society.backend.gl.controller;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.society.backend.gl.dto.JournalViewDTO;
import com.society.backend.gl.service.JournalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/journal")
@RequiredArgsConstructor
@CrossOrigin("*")
public class JournalController {

    private final JournalService service;

@GetMapping("")
public List<JournalViewDTO> getJournal(
        @RequestParam Long societyId,
        @RequestParam Long financialYearId) {

    return service.getJournal(societyId, financialYearId);
}


}
package com.society.backend.gl.controller;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/{societyId}")
    public List<JournalViewDTO> getJournal(
            @PathVariable Long societyId) {

        return service.getJournal(societyId);
    }
}
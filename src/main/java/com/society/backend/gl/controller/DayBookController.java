package com.society.backend.gl.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.society.backend.gl.dto.DayBookReportDTO;
import com.society.backend.gl.service.DayBookService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/daybook")
@CrossOrigin
@AllArgsConstructor
public class DayBookController {

    private final DayBookService dayBookService;

    @GetMapping
    public DayBookReportDTO getDayBook(
            @RequestParam Long societyId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        return dayBookService.getDayBook(societyId, date);
    }

}
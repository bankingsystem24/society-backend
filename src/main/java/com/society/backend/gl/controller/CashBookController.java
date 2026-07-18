package com.society.backend.gl.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.society.backend.gl.dto.CashBookRequest;
import com.society.backend.gl.dto.CashBookResponse;
import com.society.backend.gl.service.CashBookService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cash-book")
public class CashBookController {

    private final CashBookService cashBookService;

    @PostMapping("/report")
    public ResponseEntity<List<CashBookResponse>> getCashBook(
            @RequestBody CashBookRequest request) {

        return ResponseEntity.ok(cashBookService.getCashBook(request));
    }
}

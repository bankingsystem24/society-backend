package com.society.backend.gl.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.society.backend.gl.dto.TransferRequest;
import com.society.backend.gl.entity.TransferVoucher;
import com.society.backend.gl.service.TransferService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody TransferRequest request) {
        return ResponseEntity.ok(
                transferService.save(request)
        );
    }

    @GetMapping
    public ResponseEntity<List<TransferVoucher>> getAll(
            @RequestParam Long societyId,
            @RequestParam Long financialYearId) {

        return ResponseEntity.ok(
                transferService.getAll(societyId, financialYearId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferVoucher> getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                transferService.getById(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        transferService.delete(id);
        return ResponseEntity.ok("Transfer Voucher deleted successfully.");
    }


}

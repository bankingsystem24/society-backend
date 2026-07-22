package com.society.backend.gl.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.society.backend.dto.FlatWiseMembersDto;
import com.society.backend.gl.dto.BalanceSheetResponse;
import com.society.backend.gl.dto.Payments;
import com.society.backend.gl.service.ReportService;
import lombok.RequiredArgsConstructor;
 
@RestController
@RequestMapping("/api/reports")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/profit-loss")
    public ResponseEntity<?> getProfitAndLoss(
            @RequestParam Long societyId,Long financialYearId) {

        return ResponseEntity.ok(
                reportService.getProfitAndLoss(
                        societyId,financialYearId
                )
        );
    }

        @GetMapping("/balance-sheet")
        public BalanceSheetResponse getBalanceSheet(

            @RequestParam Long societyId,
            @RequestParam Long financialYearId) { 

                return reportService.generate(societyId, financialYearId);
        }

        @GetMapping("/payments")
        public List<Payments> getPayments(
                @RequestParam Long societyId,
                @RequestParam Long financialYearId) {

                return reportService.getPayments(societyId, financialYearId);
        }

        @GetMapping("/flat-wise-members")
        public List<FlatWiseMembersDto> flatWiseMembers(
                @RequestParam Long societyId
        ) {
                return reportService.getFlatWiseMembers(societyId);
        }
    
}

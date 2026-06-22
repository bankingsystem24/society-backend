package com.society.backend.controller;

import com.society.backend.dto.ManualPaymentRequest;
import com.society.backend.dto.MemberRequest;
import com.society.backend.dto.MemberResponse;
import com.society.backend.dto.SinkingFundResponse;
import com.society.backend.entity.Billing;
import com.society.backend.entity.Flat;
import com.society.backend.entity.Receipt;
import com.society.backend.entity.SinkingFund;
import com.society.backend.enums.PaymentStatus;
import com.society.backend.gl.dto.ContributionResponse;
import com.society.backend.gl.service.BillingService;
import com.society.backend.gl.service.ContributionService;
import com.society.backend.gl.service.JournalService;
import com.society.backend.repository.ReceiptRepository;
import com.society.backend.repository.SinkingFundRepository;
import com.society.backend.service.FlatService;
import com.society.backend.service.MemberService;
import com.society.backend.service.SinkingFundService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MemberController {

    private final MemberService memberService;
    private final FlatService flatService;
    private final BillingService billingService;
    private final SinkingFundService sinkingFundService;
    private final ContributionService contributionService;
    private final SinkingFundRepository sinkingFundRepository;
    private final JournalService journalService;
    private final ReceiptRepository receiptRepository;

    // =========================
    // CREATE MEMBER
    // =========================
    @PostMapping
    public ResponseEntity<MemberResponse> create(@RequestBody MemberRequest request) {
        return ResponseEntity.ok(memberService.createMember(request));
    }

    // =========================
    // GET ALL MEMBERS
    // =========================
    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAll(
            @RequestParam(required = false) Long societyId) {

        return ResponseEntity.ok(memberService.getAll(societyId));
    }

    // =========================
    // GET BY ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getById(id));
    }

    // =========================
    // UPDATE MEMBER
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<MemberResponse> update(
            @PathVariable Long id,
            @RequestBody MemberRequest request) {

        return ResponseEntity.ok(memberService.update(id, request));
    }

    // =========================
    // UPDATE STATUS
    // =========================
    @PutMapping("/update-status")
    public ResponseEntity<String> updateStatus(
            @RequestParam Long id,
            @RequestParam Boolean active) {

        memberService.updateStatus(id, active);
        return ResponseEntity.ok("Status updated successfully");
    }

    // =========================
    // DELETE MEMBER
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        memberService.delete(id);
        return ResponseEntity.ok("Member deleted successfully");
    }

    @GetMapping("/flats")
    public List<Flat> getMemberFlats(
            @RequestParam Long societyId,
            @RequestParam Long memberId) {
        return flatService.getFlatsForMember(societyId, memberId);
    }

    @PostMapping("/bills")
    public List<Billing> getBills(@RequestBody Map<String, Object> req) {

        List<?> rawList = (List<?>) req.get("flatIds");

        List<Long> flatIds = rawList.stream()
                .map(id -> Long.valueOf(id.toString()))
                .toList();

        Long societyId = Long.valueOf(req.get("societyId").toString());
        Long financialYearId = Long.valueOf(req.get("financialYearId").toString());

        return billingService.getBillsByFlatIds(flatIds, societyId, financialYearId);

    }

    @PostMapping("/sinking-funds") 
    public List<SinkingFundResponse> getSinkingFunds(
            @RequestBody Map<String, Object> req) {

        List<?> rawList = (List<?>) req.get("flatIds");

        List<Long> flatIds = rawList.stream()
                .map(id -> Long.valueOf(id.toString()))
                .toList();

        Long societyId = Long.valueOf(req.get("societyId").toString());
        Long financialYearId = Long.valueOf(req.get("financialYearId").toString());

        return sinkingFundService.getSinkingFundsByFlatIds(flatIds,societyId,financialYearId);
    }

    @PostMapping("/sinking-fund/manual-payment")
    public ResponseEntity<?> manualSinkingFundPayment(
            @RequestBody ManualPaymentRequest req) {

        try {

            Long financialYearId = req.getFinancialYearId();

            List<SinkingFund> sinkingFunds =
                    sinkingFundRepository.findByIdIn(req.getSinkingFundIds());

            if (sinkingFunds == null || sinkingFunds.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("No sinking fund bills found");
            }

            SinkingFund firstBill = sinkingFunds.get(0);

            double sinkingFundAmount = sinkingFunds.stream()
                    .mapToDouble(b -> b.getAmount() != null
                            ? b.getAmount()
                            : 0.0)
                    .sum();

            double totalAmount = sinkingFundAmount;

            Receipt receipt = new Receipt();

            receipt.setReceiptNo("SFRCPT-" + System.currentTimeMillis());
            receipt.setReceiptDate(LocalDate.now());
            receipt.setPaymentMode(req.getPaymentMode());
            receipt.setTransactionId(req.getTransactionId());

            receipt.setSocietyId(firstBill.getSociety().getId());
            receipt.setFlatId(firstBill.getFlat().getId());

            receipt.setTotalAmount(sinkingFundAmount);
            receipt.setTotalAmount(totalAmount);

            receipt.setFinancialYearId(financialYearId);
            receipt.setStatus(PaymentStatus.SUBMITTED);

            Receipt savedReceipt =
                    receiptRepository.save(receipt);

            String sfStatus = savedReceipt.getStatus().toString();

            for (SinkingFund sinkingFund : sinkingFunds) {

                sinkingFund.setStatus(PaymentStatus.SUBMITTED);
                sinkingFund.setPaidDate(savedReceipt.getReceiptDate());
                sinkingFund.setPaymentMode(req.getPaymentMode());
                sinkingFund.setReceiptId(savedReceipt.getId());
                sinkingFund.setTransactionId(req.getTransactionId());
            }
            
            sinkingFundRepository.saveAll(sinkingFunds);

            Long memberId = firstBill.getFlat().getOwner() != null
                    ? firstBill.getFlat().getOwner().getId()
                    : null;

            // journalService.createReceiptEntry(
            //         savedReceipt.getId(),
            //         memberId,
            //         0.0,
            //         0.0,
            //         0.0,
            //         totalAmount,
            //         req.getPaymentMode(),
            //         firstBill.getSociety().getId(),
            //         req.getUserId(),
            //         firstBill.getFlat().getId(),
            //         financialYearId
            //         );

            return ResponseEntity.ok(
                    "Sinking fund payment recorded successfully");

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.internalServerError()
                    .body(e.getMessage());
        }
    }

    @PostMapping("/contributions")
    public List<ContributionResponse> getContributions(
            @RequestBody Map<String, Object> req) {

        List<?> rawList = (List<?>) req.get("flatIds");

        List<Long> flatIds = rawList.stream()
                .map(id -> Long.valueOf(id.toString()))
                .toList();

        Long societyId = Long.valueOf(req.get("societyId").toString());
        Long financialYearId = Long.valueOf(req.get("financialYearId").toString());

        return contributionService.getContributionsByFlatIds(flatIds,societyId,financialYearId);
    }

}
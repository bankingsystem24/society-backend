package com.society.backend.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.society.backend.dto.SinkingFundOrderRequest;
import com.society.backend.dto.SinkingFundResponse;
import com.society.backend.dto.VerifySinkingFundPaymentRequest;
import com.society.backend.entity.Flat;
import com.society.backend.entity.Member;
import com.society.backend.entity.Receipt;
import com.society.backend.entity.SinkingFund;
import com.society.backend.entity.Society;
import com.society.backend.enums.PaymentStatus;
import com.society.backend.gl.service.JournalService;
import com.society.backend.repository.FlatRepository;
import com.society.backend.repository.ReceiptRepository;
import com.society.backend.repository.SinkingFundRepository;
import java.util.Objects;
import java.util.function.Function;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.razorpay.Utils;

@Service
@RequiredArgsConstructor
public class SinkingFundService {

    @Value("${razorpay.key_id}")
    private String razorpayKey;

    @Value("${razorpay.key_secret}")
    private String razorpaySecret;

    private final SinkingFundRepository sinkingFundRepository;
    private final FlatRepository flatRepository;
    private final JournalService journalService;
    private final ReceiptRepository receiptRepository;

    LocalDate today = LocalDate.now();

    // ================= GENERATE =================
    @Transactional
    public void generate(Long societyId,
            String month,
            int year,
            Double amount,
            Long createdBy,
            Long financialYearId) {

        List<Flat> flats = flatRepository.findBySociety_Id(societyId);

        Month billingMonth;
        try {
            billingMonth = Month.valueOf(month.toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Invalid month: " + month);
        }

        List<SinkingFund> existingList = sinkingFundRepository.findBySocietyIdAndMonthAndYear(
                societyId,
                month,
                year);

        for (Flat flat : flats) {

            boolean exists = existingList.stream()
                    .anyMatch(x -> x.getFlat().getId().equals(flat.getId()));

            if (exists) {
                continue;
            }

            SinkingFund sf = new SinkingFund();

            Society society = new Society();
            society.setId(societyId);

            sf.setSociety(society);
            sf.setFlat(flat);
            sf.setMonth(month);
            sf.setYear(year);
            sf.setAmount(amount);
            sf.setCreatedBy(createdBy);
            sf.setFinancialYearId(financialYearId);
            sf.setCreatedDate(
                    LocalDate.of(year, billingMonth.getValue(), 1));

            SinkingFund savedFund = sinkingFundRepository.save(sf);

            Member member = flat.getOwner();

            try {

                Long journalId = journalService.createSinkingFundEntry(
                        savedFund.getId(),
                        member,
                        amount,
                        societyId,
                        createdBy,
                        flat.getId(),
                        financialYearId);

                if (journalId == null) {
                    throw new RuntimeException(
                            "Journal not created for sinking fund "
                                    + savedFund.getId());
                }

            } catch (Exception e) {

                throw new RuntimeException(
                        "Journal failed for sinkingFundId="
                                + savedFund.getId()
                                + " -> "
                                + e.getMessage());
            }
        }
    }

    // ================= GET ALL (DTO) =================
    public List<SinkingFundResponse> getAll(Long societyId) {

        List<SinkingFund> list = sinkingFundRepository.findBySociety_Id(societyId);

        return list.stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ================= GET BY FLAT =================
    public List<SinkingFund> getByFlat(Long societyId, Long flatId) {
        return sinkingFundRepository.findBySocietyIdAndFlat_Id(societyId, flatId);
    }

    // ================= MAPPER =================
    private SinkingFundResponse mapToResponse(SinkingFund sf) {

        SinkingFundResponse dto = new SinkingFundResponse();

        dto.setId(sf.getId());
        dto.setSocietyId(sf.getSociety().getId());
        dto.setFlatId(sf.getFlat().getId());
        dto.setFlatNo(sf.getFlat().getFlatNo());

        dto.setMonth(sf.getMonth());
        dto.setYear(sf.getYear());
        dto.setAmount(sf.getAmount());
        dto.setCreatedBy(sf.getCreatedBy());
        dto.setCreatedDate(sf.getCreatedDate());
        if (sf.getFlat() != null && sf.getFlat().getOwner() != null) {
            dto.setMemberName(sf.getFlat().getOwner().getName());
        } else {
            dto.setMemberName(null);
        }
        dto.setStatus(sf.getStatus() != null ? sf.getStatus().name() : "PENDING");

        return dto;
    }

    @Transactional
    public String pay(List<Long> sinkingFundIds, String paymentMode, Long financialYearId) {

        List<SinkingFund> funds = sinkingFundRepository.findAllById(sinkingFundIds);

        if (funds.isEmpty()) {
            return "No records found";
        }

        SinkingFund first = funds.get(0);

        Long societyId = first.getSociety().getId();
        Long flatId = first.getFlat().getId();

        Double totalAmount = 0.0;

        for (SinkingFund fund : funds) {

            if (!PaymentStatus.PENDING.equals(fund.getStatus())) {
                continue;
            }

            fund.setStatus(PaymentStatus.PAID);
            fund.setPaymentMode(paymentMode);
            fund.setPaidDate(LocalDate.now());
            fund.setFinancialYearId(financialYearId);
            totalAmount += fund.getAmount();
        }

        sinkingFundRepository.saveAll(funds);

        // ================= RECEIPT =================

        Receipt receipt = new Receipt();

        receipt.setReceiptDate(LocalDate.now());
        receipt.setPaymentMode(paymentMode);

        receipt.setMaintenanceAmount(0.0);
        receipt.setInterestAmount(0.0);
        receipt.setDiscountAmount(0.0);

        // Add separate sinking fund field if available
        receipt.setTotalAmount(totalAmount);

        receipt.setSocietyId(societyId);
        receipt.setFlatId(flatId);
        receipt.setFinancialYearId(financialYearId);
        Receipt savedReceipt = receiptRepository.save(receipt);

        savedReceipt.setReceiptNo(
                "SF-" +
                        LocalDate.now().getYear() +
                        "-" +
                        savedReceipt.getId());

        savedReceipt = receiptRepository.save(savedReceipt);

        for (SinkingFund fund : funds) {
            fund.setReceiptId(savedReceipt.getId());
        }

        sinkingFundRepository.saveAll(funds);

        // ================= MEMBER =================

        Long memberId = null;

        if (first.getFlat() != null &&
                first.getFlat().getOwner() != null) {

            memberId = first.getFlat().getOwner().getId();
        }

        // ================= JOURNAL =================

        if (totalAmount > 0) {

            journalService.createReceiptEntry(
                    savedReceipt.getId(),
                    memberId,
                    totalAmount,
                    0.0,
                    0.0,
                    totalAmount,
                    paymentMode,
                    societyId,
                    0L,
                    flatId,financialYearId);
        }

        return "Sinking Fund paid successfully";
    }

    public Map<String, Object> createOrder(
            SinkingFundOrderRequest request) {

        try {

            RazorpayClient razorpay = new RazorpayClient(
                    razorpayKey,
                    razorpaySecret);

            JSONObject orderRequest = new JSONObject();

            orderRequest.put(
                    "amount",
                    request.getAmount().longValue() * 100);

            orderRequest.put("currency", "INR");

            orderRequest.put(
                    "receipt",
                    "SF_" + System.currentTimeMillis());

            Order order = razorpay.orders.create(orderRequest);

            Map<String, Object> response = new HashMap<>();

            response.put(
                    "razorpayOrderId",
                    order.get("id"));

            response.put(
                    "amount",
                    order.get("amount"));

            response.put(
                    "currency",
                    order.get("currency"));

            response.put(
                    "key",
                    razorpayKey);

            return response;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to create order", e);
        }
    }

@Transactional
public void verifyPayment(VerifySinkingFundPaymentRequest request) {


    try {

        JSONObject attributes = new JSONObject();
        attributes.put("razorpay_order_id", request.getRazorpayOrderId());
        attributes.put("razorpay_payment_id", request.getRazorpayPaymentId());
        attributes.put("razorpay_signature", request.getRazorpaySignature());

        boolean valid = Utils.verifyPaymentSignature(attributes, razorpaySecret);

        if (!valid) {
            throw new RuntimeException("Invalid Razorpay signature");
        }

        List<SinkingFund> funds =
                sinkingFundRepository.findAllById(request.getSinkingFundIds());

        if (funds.isEmpty()) {
            throw new RuntimeException("No Sinking Fund records found");
        }

        // ================= FIRST FUND =================
        SinkingFund first = funds.get(0);

        Long societyId = first.getSociety().getId();
        Long flatId = first.getFlat().getId();
        Long financialYearId = first.getFinancialYearId();

        Long memberId = (first.getFlat().getOwner() != null)
                ? first.getFlat().getOwner().getId()
                : null;

        // ================= TOTAL CALCULATION =================
        Double totalAmount = 0.0;

        for (SinkingFund fund : funds) {

            fund.setStatus(PaymentStatus.PAID);
            fund.setPaymentMode(request.getPaymentMode());
            fund.setPaidDate(LocalDate.now());
            fund.setTransactionId(request.getRazorpayPaymentId());


            totalAmount += fund.getAmount();
        }

        sinkingFundRepository.saveAll(funds);

        // ================= RECEIPT (CORRECT PLACE) =================
        Receipt receipt = new Receipt();

        receipt.setSocietyId(societyId);
        receipt.setFlatId(flatId);
        receipt.setTotalAmount(totalAmount);
        receipt.setPaymentMode(request.getPaymentMode());
        receipt.setTransactionId(request.getRazorpayPaymentId());
        receipt.setReceiptDate(LocalDate.now());
        receipt.setFinancialYearId(financialYearId);

        receipt = receiptRepository.save(receipt);

        // optional receipt number
        receipt.setReceiptNo(
                "SF-" + LocalDate.now().getYear() + "-" + receipt.getId()
        );

        receipt = receiptRepository.save(receipt);

        // attach receipt to sinking funds
        for (SinkingFund fund : funds) {
            fund.setReceiptId(receipt.getId());
        }

        sinkingFundRepository.saveAll(funds);

        // ================= JOURNAL =================
        journalService.createReceiptEntry(
                receipt.getId(),
                memberId,
                totalAmount,
                0.0,
                0.0,
                totalAmount,
                request.getPaymentMode(),
                societyId,
                request.getUserId(),
                flatId,
                financialYearId
        );

    } catch (Exception e) {
        throw new RuntimeException("Payment verification failed", e);
    }
}


    public List<SinkingFund> getSinkingFunds(
            List<Long> flatIds,Long societyId,Long financialYearId) {

        return sinkingFundRepository.findByFlat_IdInAndSocietyIdAndFinancialYearId(flatIds,societyId,financialYearId);
    }


public List<SinkingFundResponse> getSinkingFundsByFlatIds(
        List<Long> flatIds,
        Long societyId,
        Long financialYearId) {

    List<SinkingFund> sinkingFunds =
            sinkingFundRepository.findByFlat_IdInAndSocietyIdAndFinancialYearId(
                    flatIds,
                    societyId,
                    financialYearId);

    List<Long> receiptIds = sinkingFunds.stream()
            .map(SinkingFund::getReceiptId)
            .filter(Objects::nonNull)
            .distinct()
            .toList();

    Map<Long, Receipt> receiptMap = receiptRepository
            .findAllById(receiptIds)
            .stream()
            .collect(Collectors.toMap(Receipt::getId, Function.identity()));

    return sinkingFunds.stream()
            .map(sf -> {

                Receipt receipt = receiptMap.get(sf.getReceiptId());

                SinkingFundResponse dto = new SinkingFundResponse();

                dto.setId(sf.getId());
                dto.setReceiptNo(receipt != null ? receipt.getReceiptNo() : null);
                dto.setAmount(sf.getAmount());
                dto.setMonth(sf.getMonth());
                dto.setYear(sf.getYear());
                dto.setStatus(
                        sf.getStatus() != null
                                ? sf.getStatus().toString()
                                : null
                );
                dto.setPaidDate(sf.getPaidDate());
                dto.setPaymentMode(sf.getPaymentMode());
                dto.setTransactionId(sf.getTransactionId());
                dto.setFlatNo(
                        sf.getFlat() != null
                                ? sf.getFlat().getFlatNo()
                                : null
                );
                dto.setFlatId(
                    sf.getFlat() != null
                        ? sf.getFlat().getId()
                        : null
                );

                return dto;
            })
            .toList();
}
}
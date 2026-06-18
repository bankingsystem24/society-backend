package com.society.backend.gl.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import com.society.backend.dto.CompulsoryContributionRequest;
import com.society.backend.dto.ContributionOrderRequest;
import com.society.backend.dto.SinkingFundResponse;
import com.society.backend.dto.VerifyContributionPaymentRequest;
import com.society.backend.entity.Flat;
import com.society.backend.entity.Member;
import com.society.backend.entity.Receipt;
import com.society.backend.entity.Society;
import com.society.backend.enums.PaymentStatus;
import com.society.backend.gl.dto.ContributionResponse;
import com.society.backend.gl.entity.Contribution;
import com.society.backend.gl.repository.ContributionRepository;
import com.society.backend.repository.FlatRepository;
import com.society.backend.repository.ReceiptRepository;
import com.society.backend.repository.SocietyRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContributionService {

    private final ContributionRepository contributionRepository;
    private final FlatRepository flatRepository;
    private final JournalService journalService;
    private final ReceiptRepository receiptRepository;
    private final SocietyRepository societyRepository;

    @Value("${razorpay.key_id}")
    private String razorpayKey;

    @Value("${razorpay.key_secret}")
    private String razorpaySecret;

    public List<ContributionResponse> getContributions(Long societyId, Long financialYearId) {

        List<Contribution> list = contributionRepository.findBySocietyIdAndFinancialYearId(societyId,
                financialYearId);

        return list.stream().map(c -> {

            ContributionResponse dto = new ContributionResponse();

            dto.setId(c.getId());
            dto.setName(c.getName());
            dto.setAmount(c.getAmount());
            dto.setMode(c.getMode());
            dto.setDate(c.getDate());
            dto.setDueDate(c.getDueDate());
            dto.setStatus(c.getStatus().name());
            if (c.getSociety() != null) {
                dto.setSocietyId(c.getSociety().getId());
            }
            dto.setMemberId(c.getMemberId());
            dto.setFinancialYearId(financialYearId);
            dto.setType(c.getType());
            dto.setDescription(c.getDescription());

            if (c.getFlat() != null) {
                dto.setFlatId(c.getFlat().getId());
                dto.setFlatNo(c.getFlat().getFlatNo());
                dto.setAreaSqFt(c.getFlat().getAreaSqFt());
            }

            return dto;

        }).toList();
    }

    public void createCompulsoryContribution(Long societyId, Long financialYearId, CompulsoryContributionRequest req) {

        List<Flat> flats = flatRepository.findBySociety_Id(societyId);

        Society society = societyRepository.findById(societyId)
                .orElseThrow(() -> new RuntimeException("Society not found"));
        List<Contribution> contributions = new ArrayList<>();

        for (Flat f : flats) {

            double amount;

            if ("FLAT".equalsIgnoreCase(req.getMode())) {
                amount = req.getFlatAmount();
            } else {
                amount = f.getAreaSqFt() * req.getRate();
            }

            Contribution c = new Contribution();
            c.setSociety(society);
            c.setMemberId(f.getId());
            c.setName(req.getName());
            c.setType("COMPULSORY");
            c.setMode(req.getMode());
            c.setFlat(f);
            c.setAmount(amount);
            c.setDueDate(req.getDueDate());
            c.setDate(req.getDate());
            c.setDescription(req.getDescription());
            c.setCreatedBy(req.getUserId());
            c.setFinancialYearId(financialYearId);

            contributions.add(c);
        }

        // ✅ SAVE ONLY ONCE
        List<Contribution> savedContributions = contributionRepository.saveAll(contributions);

        // ✅ JOURNAL POSTING
        for (Contribution c : savedContributions) {

            Member member = c.getFlat().getOwner();

            try {

                Long journalId = journalService.createContributionEntry(
                        c.getId(),
                        member,
                        c.getAmount(),
                        societyId,
                        c.getCreatedBy(),
                        c.getFlat().getId(),
                        c.getFinancialYearId());

                if (journalId == null) {
                    throw new RuntimeException(
                            "Journal not created for contribution " + c.getId());
                }

            } catch (Exception e) {

                throw new RuntimeException(
                        "Journal failed for contributionId="
                                + c.getId()
                                + " -> " + e.getMessage());
            }
        }
    }

    public void createVoluntaryContribution(Long societyId, Long financialYearId, CompulsoryContributionRequest req) {

        List<Flat> flats = flatRepository.findBySociety_Id(societyId);

        Society society = societyRepository.findById(societyId)
                .orElseThrow(() -> new RuntimeException("Society not found"));

        List<Contribution> contributions = new ArrayList<>();

        for (Flat f : flats) {

            double amount;

            if ("FLAT".equalsIgnoreCase(req.getMode())) {
                amount = req.getMinAmount();
            } else {
                amount = f.getAreaSqFt() * req.getRate();
            }

            Contribution c = new Contribution();
            c.setSociety(society);
            c.setMemberId(f.getOwner().getId());
            c.setName(req.getName());
            c.setType("VOLUNTARY");
            c.setMode(req.getMode());
            c.setFlat(f);
            c.setAmount(amount);
            c.setDueDate(req.getDueDate());
            c.setDate(req.getDate());
            c.setDescription(req.getDescription());
            c.setCreatedBy(req.getUserId());
            c.setFinancialYearId(financialYearId);
            c.setDescription(req.getDescription());

            contributions.add(c);
        }

        contributionRepository.saveAll(contributions);

        // ✅ SAVE ONLY ONCE
        // List<Contribution> savedContributions =
        // contributionRepository.saveAll(contributions);

        // // ✅ JOURNAL POSTING
        // for (Contribution c : savedContributions) {

        // Member member = c.getFlat().getOwner();

        // try {

        // Long journalId = journalService.createContributionEntry(
        // c.getId(),
        // member,
        // c.getAmount(),
        // societyId,
        // c.getCreatedBy(),
        // c.getFlat().getId(),
        // c.getFinancialYearId());

        // if (journalId == null) {
        // throw new RuntimeException(
        // "Journal not created for contribution " + c.getId());
        // }

        // } catch (Exception e) {

        // throw new RuntimeException(
        // "Journal failed for contributionId="
        // + c.getId()
        // + " -> " + e.getMessage());
        // }
        // }

    }

    @Transactional
    public String pay(List<Long> contributionIds,
            String paymentMode,
            Long financialYearId,
            Double contributionAmount,
            Long userId) {

        List<Contribution> contributions = contributionRepository.findAllById(contributionIds);

        if (contributions.isEmpty()) {
            return "No records found";
        }

        Contribution first = contributions.get(0);

        Long societyId = first.getSociety().getId();
        Long flatId = first.getFlat().getId();

        Double totalAmount = 0.00;

        for (Contribution contribution : contributions) {

            if (!PaymentStatus.PENDING.equals(contribution.getStatus())) {
                continue;
            }

            contribution.setStatus(PaymentStatus.PAID);
            contribution.setPaymentMode(paymentMode);
            contribution.setFinancialYearId(financialYearId);

        }

        contributionRepository.saveAll(contributions);

        // ================= RECEIPT =================

        Receipt receipt = new Receipt();

        receipt.setReceiptDate(LocalDate.now());
        receipt.setPaymentMode(paymentMode);

        receipt.setMaintenanceAmount(0.0);
        receipt.setInterestAmount(0.0);
        receipt.setDiscountAmount(0.0);

        receipt.setTotalAmount(contributionAmount);

        receipt.setSocietyId(societyId);
        receipt.setFlatId(flatId);
        receipt.setFinancialYearId(financialYearId);

        Receipt savedReceipt = receiptRepository.save(receipt);

        savedReceipt.setReceiptNo(
                "CON-" +
                        LocalDate.now().getYear() +
                        "-" +
                        savedReceipt.getId());

        savedReceipt = receiptRepository.save(savedReceipt);

        // ================= UPDATE CONTRIBUTIONS =================

        for (Contribution contribution : contributions) {
            contribution.setReceiptId(savedReceipt.getId());
        }

        contributionRepository.saveAll(contributions);

        // ================= MEMBER =================

        Long memberId = null;

        if (first.getFlat() != null &&
                first.getFlat().getOwner() != null) {

            memberId = first.getFlat().getOwner().getId();
        }

        // ================= JOURNAL ENTRY =================

        if (contributionAmount > 0) {

            journalService.createReceiptEntry(
                    savedReceipt.getId(),
                    memberId,
                    totalAmount,
                    0.0,
                    0.0,
                    contributionAmount,
                    paymentMode,
                    societyId,
                    userId,
                    flatId,
                    financialYearId);
        }

        return "Contribution paid successfully";
    }

    public Map<String, Object> createOrder(
            ContributionOrderRequest request) {

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
                    "CON_" + System.currentTimeMillis());

            Order order = razorpay.orders.create(orderRequest);

            Map<String, Object> response = new HashMap<>();

            response.put(
                    "razorpayOrderId",
                    order.get("id"));

            response.put(
                    "amount",
                    request.getAmount());

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
    public void verifyPayment(VerifyContributionPaymentRequest request) {

        System.out.println("Request:" + request);

        try {

            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", request.getRazorpayOrderId());
            attributes.put("razorpay_payment_id", request.getRazorpayPaymentId());
            attributes.put("razorpay_signature", request.getRazorpaySignature());

            boolean valid = Utils.verifyPaymentSignature(attributes, razorpaySecret);

            if (!valid) {
                throw new RuntimeException("Invalid Razorpay signature");
            }

            List<Contribution> contributions = contributionRepository.findAllById(request.getContributionIds());

            if (contributions.isEmpty()) {
                throw new RuntimeException("No Contribution  records found");
            }

            // ================= FIRST FUND =================
            Contribution first = contributions.get(0);

            Long societyId = first.getSociety().getId();
            Long flatId = first.getFlat().getId();
            Long financialYearId = first.getFinancialYearId();
            String contributionType = request.getType();

            Long memberId = (first.getFlat().getOwner() != null)
                    ? first.getFlat().getOwner().getId()
                    : null;

            // ================= TOTAL CALCULATION =================
            Double totalAmount = 0.0;

            for (Contribution contribution : contributions) {

                contribution.setStatus(PaymentStatus.PAID);
                contribution.setPaymentMode(request.getPaymentMode());
                contribution.setTransactionId(request.getRazorpayPaymentId());

                totalAmount += contribution.getAmount();
            }

            contributionRepository.saveAll(contributions);

            // For voluntary contribution use amount entered in modal
            if ("VOLUNTARY".equalsIgnoreCase(contributionType)) {
                totalAmount = request.getAmount();
            }

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
                    "CON-" + LocalDate.now().getYear() + "-" + receipt.getId());

            receipt = receiptRepository.save(receipt);

            for (Contribution contribution : contributions) {
                contribution.setReceiptId(receipt.getId());
            }

            contributionRepository.saveAll(contributions);

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
                    financialYearId);

        } catch (Exception e) {
            throw new RuntimeException("Payment verification failed", e);
        }
    }

    public List<ContributionResponse> getContributionsByFlatIds(
        List<Long> flatIds,
        Long societyId,
        Long financialYearId) {

    return contributionRepository
            .findByFlat_IdInAndSocietyIdAndFinancialYearId(
                    flatIds,
                    societyId,
                    financialYearId
            )
            .stream()
            .map(c -> {

                ContributionResponse dto = new ContributionResponse();

                dto.setId(c.getId());

                Receipt receipt = null;

                if (c.getReceiptId() != null) {
                    receipt = receiptRepository
                            .findById(c.getReceiptId())
                            .orElse(null);
                }

                dto.setReceiptNo(
                        receipt != null
                                ? receipt.getReceiptNo()
                                : null
                );

                if (receipt != null) {
                    dto.setContributionAmount(c.getAmount());
                    dto.setReceiptAmount(receipt.getTotalAmount());
                } else {
                    dto.setContributionAmount(0.0); 
                    dto.setReceiptAmount(0.0);
                }

                dto.setStatus(
                        c.getStatus() != null
                                ? c.getStatus().toString()
                                : null
                );

                dto.setPaymentMode(c.getPaymentMode());
                dto.setTransactionId(c.getTransactionId());
                dto.setFlatNo(c.getFlat() != null ? c.getFlat().getFlatNo() : null );
                dto.setPaidDate(
                    receipt != null
                        ? receipt.getReceiptDate()
                        : null
                );
                return dto;
            })
            .toList();
}

}

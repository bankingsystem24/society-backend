package com.society.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.society.backend.dto.BillingResponse;
import com.society.backend.dto.ReceiptRequest;
import com.society.backend.dto.ReceiptResponse;
import com.society.backend.entity.Billing;
import com.society.backend.entity.Receipt;
import com.society.backend.repository.BillingRepository;
import com.society.backend.repository.ReceiptRepository;

@Service
public class ReceiptService {

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private BillingRepository billingRepository;

    @Transactional
    public Receipt createReceipt(ReceiptRequest req) {

        // 1. SAVE RECEIPT
        Receipt receipt = new Receipt();
        receipt.setReceiptNo(req.getReceiptNo());
        receipt.setSocietyId(req.getSocietyId());
        receipt.setFlatId(req.getFlatId());
        receipt.setTotalAmount(req.getTotalAmount());
        receipt.setPaymentMode(req.getPaymentMode());
        receipt.setMaintenanceAmount(req.getMaintenanceAmount());
        receipt.setInterestAmount(req.getInterestAmount());
        receipt.setDiscountAmount(req.getDiscountAmount());

        Receipt savedReceipt = receiptRepository.save(receipt);
      

        return savedReceipt;
    }

public List<ReceiptResponse> viewReceipts(
        Long societyId,
        Long flatId
) {

    List<Receipt> receipts = receiptRepository.findBySocietyId(societyId);

    // optional flat filter
    if (flatId != null) {
        receipts = receipts.stream()
                .filter(r -> r.getFlatId().equals(flatId))
                .toList();
    }

    return receipts.stream().map(r -> {

        ReceiptResponse dto = new ReceiptResponse();

        dto.setId(r.getId());
        dto.setReceiptNo(r.getReceiptNo());
        dto.setCreatedAt(r.getCreatedAt().toLocalDate());

        dto.setFlatId(r.getFlatId());
        dto.setMaintenanceAmount(r.getMaintenanceAmount());
        dto.setInterestAmount(r.getInterestAmount());
        dto.setDiscountAmount(r.getDiscountAmount());

        dto.setTotalAmount(r.getTotalAmount());
        dto.setTransactionId(r.getTransactionId());
        // payment mode
        List<Billing> bills =
                billingRepository.findByReceiptId(r.getId());

        if (!bills.isEmpty()) {

            Billing bill = bills.get(0);

            dto.setPaymentMode(bill.getPaymentMode());

            if (bill.getFlat() != null) {

                dto.setFlatNo(bill.getFlat().getFlatNo());

                if (bill.getFlat().getOwner() != null) {

                    dto.setMemberId(
                            bill.getFlat().getOwner().getId()
                    );

                    dto.setMemberName(
                            bill.getFlat().getOwner().getName()
                    );
                }
            }
        }

        return dto;

    }).toList();
}

public List<BillingResponse> getReceiptDetails(Long receiptId) {

    List<Billing> bills =
            billingRepository.findByReceiptId(receiptId);

    return bills.stream().map(b -> {

        BillingResponse dto = new BillingResponse();

        dto.setId(b.getId());

        dto.setMonth(b.getMonth());
        dto.setYear(b.getYear());

        dto.setMaintenanceAmount(
                b.getMaintenanceAmount()
        );
        dto.setInterestAmount(
                b.getInterestAmount()
        );
        dto.setDiscountAmount(
                b.getDiscountAmount()
        );
        dto.setPenaltyAmount(
                b.getPenaltyAmount()
        );

        dto.setTotalAmount(
                b.getTotalAmount()
        );

        dto.setStatus(
                b.getStatus().name()
        );

        if (b.getFlat() != null) {

            dto.setFlatId(
                    b.getFlat().getId()
            );

            dto.setFlatNo(
                    b.getFlat().getFlatNo()
            );

            if (b.getFlat().getOwner() != null) {

                dto.setMemberId(
                        b.getFlat().getOwner().getId()
                );

                dto.setMemberName(
                        b.getFlat().getOwner().getName()
                );
            }
        }

        return dto;

    }).toList();
}

}

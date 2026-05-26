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
import com.society.backend.entity.ReceiptItem;
import com.society.backend.enums.PaymentStatus;
import com.society.backend.repository.BillingRepository;
import com.society.backend.repository.ReceiptItemRepository;
import com.society.backend.repository.ReceiptRepository;

@Service
public class ReceiptService {

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private ReceiptItemRepository receiptItemRepository;

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

        Receipt savedReceipt = receiptRepository.save(receipt);

        // 2. PROCESS EACH BILL
        for (Long billId : req.getBillIds()) {

            // a) Save mapping table (ReceiptItem)
            ReceiptItem item = new ReceiptItem();
            item.setReceiptId(savedReceipt.getId());
            item.setBillId(billId);
            receiptItemRepository.save(item);
        }
        billingRepository.updateReceiptAndStatus(
                savedReceipt.getId(),
                PaymentStatus.PAID,
                req.getBillIds()
        );
        

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

        dto.setTotalAmount(r.getTotalAmount());

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

package com.society.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.society.backend.dto.ReceiptRequest;
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

}

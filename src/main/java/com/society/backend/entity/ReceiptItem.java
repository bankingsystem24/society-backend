package com.society.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "receipt_items")
public class ReceiptItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "receipt_id")
    private Long receiptId;

    @Column(name = "bill_id")
    private Long billId;

    // ================= GETTERS =================

    public Long getId() {
        return id;
    }

    public Long getReceiptId() {
        return receiptId;
    }

    public Long getBillId() {
        return billId;
    }

    // ================= SETTERS =================

    public void setId(Long id) {
        this.id = id;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }
}

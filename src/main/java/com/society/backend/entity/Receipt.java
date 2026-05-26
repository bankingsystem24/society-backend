package com.society.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "receipts")
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "receipt_no")
    private String receiptNo;

    @Column(name = "society_id")
    private Long societyId;

    @Column(name = "flat_id")
    private Long flatId;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // ================= GETTERS =================

    public Long getId() {
        return id;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public Long getSocietyId() {
        return societyId;
    }

    public Long getFlatId() {
        return flatId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // ================= SETTERS =================

    public void setId(Long id) {
        this.id = id;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public void setSocietyId(Long societyId) {
        this.societyId = societyId;
    }

    public void setFlatId(Long flatId) {
        this.flatId = flatId;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
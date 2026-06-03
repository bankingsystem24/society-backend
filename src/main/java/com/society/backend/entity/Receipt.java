package com.society.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
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

    
    @Column(name = "payment_mode")
    private String paymentMode;

    @Column(name = "maintenance_amount")
    private Double maintenanceAmount;

    @Column(name = "interest_amount")
    private Double interestAmount;

    @Column(name = "discount_amount")
    private Double discountAmount;

    private String transactionId;

    private LocalDate receiptDate;
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


    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDate getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDate receiptDate) {
        this.receiptDate = receiptDate;
    }

    public Double getMaintenanceAmount() {
        return maintenanceAmount;
    }

    public void setMaintenanceAmount(Double maintenanceAmount) {
        this.maintenanceAmount = maintenanceAmount;
    }

    public Double getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(Double interestAmount) {
        this.interestAmount = interestAmount;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    

}
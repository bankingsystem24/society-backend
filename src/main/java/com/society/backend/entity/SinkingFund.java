package com.society.backend.entity;
import java.time.LocalDate;
import com.society.backend.enums.PaymentStatus;
import jakarta.persistence.*;
@Entity
@Table(name = "sinking_fund", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "society_id", "flat_id", "month", "year" })
})
public class SinkingFund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Society
    @ManyToOne
    @JoinColumn(name = "society_id", nullable = false)
    private Society society;
    // flat
    @ManyToOne
    @JoinColumn(name = "flat_id", nullable = false)
    private Flat flat;
    // Billing period
    @Column(nullable = false)
    private String month;
    @Column(nullable = false)
    private int year;
    // Amounts
    private Double amount = 0.0;
    // Audit
    private Long createdBy;
    private LocalDate createdDate;
    // Payment status
    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING;
    private LocalDate paidDate;
    @Column(name = "receipt_id")
    private Long receiptId;
    @Column(name = "payment_mode")
    private String paymentMode;
    private String transactionId;
    // ================= LIFECYCLE =================
    @PrePersist
    public void prePersist() {

        if (this.createdDate == null) {
            this.createdDate = LocalDate.now();
        }
        if (this.status == null) {
            this.status = PaymentStatus.PENDING;
        }
    }

    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public Flat getFlat() {
        return flat;
    }

    public void setFlat(Flat flat) {
        this.flat = flat;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDate getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDate paidDate) {
        this.paidDate = paidDate;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getTransactionId() { return transactionId; }

    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public Long getReceiptId() {
        return receiptId;
    }
    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }
}

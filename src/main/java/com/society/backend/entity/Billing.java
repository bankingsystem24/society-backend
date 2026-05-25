package com.society.backend.entity;
import java.time.LocalDate;
import com.society.backend.enums.PaymentStatus;
import jakarta.persistence.*;

@Entity
@Table(
    name = "billing",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"society_id", "flat_id", "month", "year"})
    }
)
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Society
    @ManyToOne
    @JoinColumn(name = "society_id", nullable = false)
    private Society society;

    // Flat
    @ManyToOne
    @JoinColumn(name = "flat_id", nullable = false)
    private Flat flat;

    // Billing period
    @Column(nullable = false)
    private String month;   // JANUARY

    @Column(nullable = false)
    private int year;

    // Amount details
    private Double maintenanceAmount = 0.0;
    private Double penaltyAmount = 0.0;
    private Double totalAmount = 0.0;
    @Column(name = "receipt_id")
    private Long receiptId;

    // Payment status
    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING;

    private LocalDate dueDate;

    private LocalDate paidDate;

    private String paymentMode;

    // Audit fields
    private LocalDate createdDate;

    // ================= LIFECYCLE =================

    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDate.now();
        this.status = PaymentStatus.PENDING;
        this.totalAmount = calculateTotal();
    }

    @PreUpdate
    public void preUpdate() {
        this.totalAmount = calculateTotal();
    }

    // ================= BUSINESS LOGIC =================

    @Transient
    public Double calculateTotal() {
        double maintenance = maintenanceAmount != null ? maintenanceAmount : 0;
        double penalty = penaltyAmount != null ? penaltyAmount : 0;
        return maintenance + penalty;
    }

    // ================= GETTERS & SETTERS =================

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Society getSociety() { return society; }

    public void setSociety(Society society) { this.society = society; }

    public Flat getFlat() { return flat; }

    public void setFlat(Flat flat) { this.flat = flat; }

    public String getMonth() { return month; }

    public void setMonth(String month) { this.month = month; }

    public int getYear() { return year; }

    public void setYear(int year) { this.year = year; }

    public Double getMaintenanceAmount() { return maintenanceAmount; }

    public void setMaintenanceAmount(Double maintenanceAmount) {
        this.maintenanceAmount = maintenanceAmount;
    }

    public Double getPenaltyAmount() { return penaltyAmount; }

    public void setPenaltyAmount(Double penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public Double getTotalAmount() { return totalAmount; }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PaymentStatus getStatus() { return status; }

    public void setStatus(PaymentStatus status) { this.status = status; }

    public LocalDate getDueDate() { return dueDate; }

    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getPaidDate() { return paidDate; }

    public void setPaidDate(LocalDate paidDate) { this.paidDate = paidDate; }

    public String getPaymentMode() { return paymentMode; }

    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public LocalDate getCreatedDate() { return createdDate; }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
    public Long getReceiptId() {
        return receiptId;
    }
    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }
}
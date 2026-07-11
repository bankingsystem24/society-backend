package com.society.backend.gl.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "profit_loss_snapshot_header")
public class ProfitLossSnapshotHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "society_id", nullable = false)
    private Long societyId;

    @Column(name = "financial_year_id", nullable = false, unique = true)
    private Long financialYearId;

    @Column(name = "total_income", nullable = false)
    private Double totalIncome = 0.0;

    @Column(name = "total_expense", nullable = false)
    private Double totalExpense = 0.0;

    @Column(name = "net_profit_loss", nullable = false)
    private Double netProfitLoss = 0.0;

    @Column(length = 500)
    private String remarks;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(
            mappedBy = "header",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<ProfitLossSnapshotDetail> details = new ArrayList<>();

    public ProfitLossSnapshotHeader() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Long societyId) {
        this.societyId = societyId;
    }

    public Long getFinancialYearId() {
        return financialYearId;
    }

    public void setFinancialYearId(Long financialYearId) {
        this.financialYearId = financialYearId;
    }

    public Double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public Double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(Double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public Double getNetProfitLoss() {
        return netProfitLoss;
    }

    public void setNetProfitLoss(Double netProfitLoss) {
        this.netProfitLoss = netProfitLoss;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<ProfitLossSnapshotDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ProfitLossSnapshotDetail> details) {
        this.details = details;
    }
}
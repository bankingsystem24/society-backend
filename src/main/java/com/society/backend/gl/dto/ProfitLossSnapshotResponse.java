package com.society.backend.gl.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ProfitLossSnapshotResponse {

    private Long id;
    private Long societyId;
    private Long financialYearId;

    private Double totalIncome;
    private Double totalExpense;
    private Double netProfitLoss;

    private String remarks;
    private Long createdBy;
    private LocalDateTime createdAt;

    private List<ProfitLossSnapshotDetailResponse> details;

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

    public List<ProfitLossSnapshotDetailResponse> getDetails() {
        return details;
    }

    public void setDetails(List<ProfitLossSnapshotDetailResponse> details) {
        this.details = details;
    }
}
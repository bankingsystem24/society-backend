package com.society.backend.gl.dto;

import java.util.List;

public class ProfitLossSnapshotRequest {

    private Long societyId;
    private Long financialYearId;
    private String remarks;
    private Long createdBy;

    private List<ProfitLossSnapshotDetailRequest> details;

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

    public List<ProfitLossSnapshotDetailRequest> getDetails() {
        return details;
    }

    public void setDetails(List<ProfitLossSnapshotDetailRequest> details) {
        this.details = details;
    }
}
package com.society.backend.gl.dto;

import java.util.List;

public class SaveTrialBalanceRequest {

    private Long societyId;

    private Long financialYearId;

    private Long createdBy;

    private String remarks;

    private List<TrialBalanceDetailDTO> details;

    // ================= GETTERS & SETTERS =================

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

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<TrialBalanceDetailDTO> getDetails() {
        return details;
    }

    public void setDetails(List<TrialBalanceDetailDTO> details) {
        this.details = details;
    }
}
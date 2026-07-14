package com.society.backend.gl.dto;

import java.util.List;

public class SaveBalanceSheetRequest {

    private Long societyId;
    private Long financialYearId;
    private String financialYear;
    private String createdBy;
    private List<BalanceSheetDetailDTO> details;

    public SaveBalanceSheetRequest() {
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

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<BalanceSheetDetailDTO> getDetails() {
        return details;
    }

    public void setDetails(List<BalanceSheetDetailDTO> details) {
        this.details = details;
    }
}
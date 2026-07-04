package com.society.backend.dto;

import com.society.backend.enums.PaymentStatus;

import jakarta.persistence.Column;

public class BillingFilterRequest {

    private Long societyId;
    private Long flatId;
    private Integer fromYear;
    private String month;
    private PaymentStatus status;
    private Long memberId;
    @Column(name = "financial_year_id", nullable = false)
    private Long financialYearId;

    public Long getFinancialYearId() {
        return financialYearId;
    }

    public void setFinancialYearId(Long financialYearId) {
        this.financialYearId = financialYearId;
    }

    public Long getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Long societyId) {
        this.societyId = societyId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getFlatId() {
        return flatId;
    }

    public void setFlatId(Long flatId) {
        this.flatId = flatId;
    }

    public Integer getFromYear() {
        return fromYear;
    }

    public void setFromYear(Integer fromYear) {
        this.fromYear = fromYear;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
package com.society.backend.dto;
import java.util.List;

import jakarta.persistence.Column;

public class PaymentRequest {

    private List<Long> billIds;
    private String paymentMode;
    private Long memberId;
    @Column(name = "financial_year_id", nullable = false)
    private Long financialYearId;

    public Long getFinancialYearId() {
        return financialYearId;
    }

    public void setFinancialYearId(Long financialYearId) {
        this.financialYearId = financialYearId;
    }

    public List<Long> getBillIds() {
        return billIds;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId){
        this.memberId = memberId;
    }

    public void setBillIds(List<Long> billIds) {
        this.billIds = billIds;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }
}
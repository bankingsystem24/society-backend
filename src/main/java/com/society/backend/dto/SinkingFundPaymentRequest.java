package com.society.backend.dto;

import java.util.List;

import jakarta.persistence.Column;

public class SinkingFundPaymentRequest {

    private List<Long> sinkingFundIds;
    private String paymentMode;
    @Column(name = "financial_year_id", nullable = false)
    private Long financialYearId;

    public Long getFinancialYearId() {
        return financialYearId;
    }

    public void setFinancialYearId(Long financialYearId) {
        this.financialYearId = financialYearId;
    }

    public List<Long> getSinkingFundIds() {
        return sinkingFundIds;
    }

    public void setSinkingFundIds(List<Long> sinkingFundIds) {
        this.sinkingFundIds = sinkingFundIds;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }
}

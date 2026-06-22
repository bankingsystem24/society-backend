package com.society.backend.dto;

import java.util.List;

public class ManualPaymentRequest {

    private List<Long> billIds;
    private List<Long> sinkingFundIds;
    private List<Long> contributionIds;
    private Long memberId;
    private Double amount;
    private String paymentMode;
    private String transactionId;
    private Long financialYearId;
    private Long userId;

    public List<Long> getBillIds() {
        return billIds;
    }

    public void setBillIds(List<Long> billIds) {
        this.billIds = billIds;
    }

    public List<Long> getSinkingFundIds(){ return sinkingFundIds;}
    public void setSinkingFundIds(List<Long> sinkingFundIds){ this.sinkingFundIds = sinkingFundIds;}

    public List<Long> getContributionIds(){ return contributionIds;}
    public void setContributionIds(List<Long> contributionIds){ this.contributionIds = contributionIds;}

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Long getFinancialYearId() {
        return financialYearId;
    }

    public void setFinancialYearId(Long financialYearId) {
        this.financialYearId = financialYearId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

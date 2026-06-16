package com.society.backend.gl.dto;
import java.math.BigDecimal;
import java.util.List;

public class ContributionPaymentRequest {

    private List<Long> contributionIds;
    private String paymentMode;
    private Long financialYearId;
    private Double voluntaryAmount;

    public List<Long> getContributionIds() {
        return contributionIds;
    }

    public void setContributionIds(List<Long> contributionIds) {
        this.contributionIds = contributionIds;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Long getFinancialYearId() {
        return financialYearId;
    }

    public void setFinancialYearId(Long financialYearId) {
        this.financialYearId = financialYearId;
    }

    public Double getVoluntaryAmount(){
        return voluntaryAmount;
    }

    public void setVoluntaryAmount(Double voluntaryAmount){
        this.voluntaryAmount=voluntaryAmount;
    }
}

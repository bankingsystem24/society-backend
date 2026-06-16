package com.society.backend.gl.dto;
import java.util.List;

public class ContributionPaymentRequest {

    private List<Long> contributionIds;
    private String paymentMode;
    private Long financialYearId;
    private Double contributionAmount;
    private Long userId;

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

    public Double getContributionAmount(){
        return contributionAmount;
    }

    public void setContributionAmount(Double contributionAmount){
        this.contributionAmount=contributionAmount;
    }

    public Long getUserId(){
        return userId;
    }

    public void setUserId(Long userId){
        this.userId = userId;
    }
}

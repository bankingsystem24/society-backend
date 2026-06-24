package com.society.backend.gl.dto;
import java.util.List;

import jakarta.persistence.Column;
 
public class ContributionPaymentRequest {

    private List<Long> contributionIds;
    private String paymentMode;
    private Long financialYearId;
    private Double contributionAmount;
    private Long userId;
    @Column(name = "gl_receivable")
    private Integer glReceivable;
    @Column(name = "gl_credit_account")
    private Integer glCreditAccount;

    public Integer getGlReceivable(){return glReceivable;}
    public void setGlReceivable(Integer glReceivable){ this.glReceivable = glReceivable;}
    
    public Integer getGlCreditAccount(){return glCreditAccount;}
    public void setGlCreditAccount(Integer glCreditAccount){ this.glCreditAccount = glCreditAccount;}

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

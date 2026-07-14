package com.society.backend.dto;

import java.util.List;

import jakarta.persistence.Column;

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
    @Column(name = "gl_receivable")
    private Integer glReceivable;
    @Column(name = "gl_credit_account")
    private Integer glCreditAccount;
    @Column(name = "gl_cash_in_hand")
    private Integer glCashInHand;
    @Column(name = "gl_bank_account")
    private Integer glBankAccount;
    @Column(name = "gl_interest_income")
    private Integer glInterestIncome;
    @Column(name = "gl_discount")
    private Integer glDiscount;
    private Double interestAmount;
    private Double discountAmount;
    private Long societyId;
    private Integer selectedCount;

    public Double getInterestAmount(){return interestAmount;}
    public Double getDiscountAmount(){return discountAmount;}
    public void setInterestAmount(Double interestAmount){this.interestAmount=interestAmount;}
    public void setDiscountAmount(double discountAmount){this.discountAmount =discountAmount;}

    public Integer getGlReceivable(){return glReceivable;}
    public void setGlReceivable(Integer glReceivable){ this.glReceivable = glReceivable;}
    public Integer getGlCreditAccount(){return glCreditAccount;}
    public void setGlCreditAccount(Integer glCreditAccount){ this.glCreditAccount = glCreditAccount;}
    public Integer getGlCashInHand(){ return glCashInHand;}
    public Integer getGlBankAccount(){ return glBankAccount;}
    public Integer getGlInterestIncome(){ return glInterestIncome;}
    public Integer getGlDiscount(){ return glDiscount;}

    public void setGlCashInHand(Integer glCashInHand){ this.glCashInHand = glCashInHand;}
    public void setGlBankAccount(Integer glBankAccount){ this.glBankAccount = glBankAccount;}
    public void setGlInterestIncome(Integer glInterestIncome){ this.glInterestIncome = glInterestIncome;}
    public void setGlDiscount(Integer glDiscount){ this.glDiscount = glDiscount;}

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

    public Long getSocietyId(){return societyId;}
    public void setSocietyId(Long societyId){this.societyId = societyId;}

    public Integer getSelectedCount(){return selectedCount;}
    public void setSelectedCount(Integer selectedCount){this.selectedCount = selectedCount;}
}

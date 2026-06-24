package com.society.backend.dto;

import java.util.List;

import jakarta.persistence.Column;

public class VerifySinkingFundPaymentRequest {

    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;

    private List<Long> sinkingFundIds;

    private Long memberId;
    private Long userId;
    private Double amount;
    private String paymentMode;
    @Column(name = "financial_year_id", nullable = false)
    private Long financialYearId;

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

    public Integer getGlCashInHand(){ return glCashInHand;}
    public Integer getGlBankAccount(){ return glBankAccount;}
    public Integer getGlInterestIncome(){ return glInterestIncome;}
    public Integer getGlDiscount(){ return glDiscount;}

    public void setGlCashInHand(Integer glCashInHand){ this.glCashInHand = glCashInHand;}
    public void setGlBankAccount(Integer glBankAccount){ this.glBankAccount = glBankAccount;}
    public void setGlInterestIncome(Integer glInterestIncome){ this.glInterestIncome = glInterestIncome;}
    public void setGlDiscount(Integer glDiscount){ this.glDiscount = glDiscount;}
    
    public Integer getGlReceivable(){return glReceivable;}
    public void setGlReceivable(Integer glReceivable){ this.glReceivable = glReceivable;}
    
    public Integer getGlCreditAccount(){return glCreditAccount;}
    public void setGlCreditAccount(Integer glCreditAccount){ this.glCreditAccount = glCreditAccount;}


    public Long getFinancialYearId() {
        return financialYearId;
    }

    public void setFinancialYearId(Long financialYearId) {
        this.financialYearId = financialYearId;
    }

    // getters setters
    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }

    public String getRazorpaySignature() {
        return razorpaySignature;
    }

    public void setRazorpaySignature(String razorpaySignature) {
        this.razorpaySignature = razorpaySignature;
    }

    public List<Long> getSinkingFundIds() {
        return sinkingFundIds;
    }

    public void setSinkingFundIds(List<Long> sinkingFundIds) {
        this.sinkingFundIds = sinkingFundIds;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserrId(Long userId) {
        this.userId = userId;
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

}

package com.society.backend.dto;

import java.util.List;

import jakarta.persistence.Column; 

public class SinkingFundPaymentRequest {

    private List<Long> sinkingFundIds;
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

    @Column(name = "transaction_Id")
    private String transactionId;

    public Integer getGlCashInHand(){ return glCashInHand;}
    public Integer getGlBankAccount(){ return glBankAccount;}
    public Integer getGlInterestIncome(){ return glInterestIncome;}
    public Integer getGlDiscount(){ return glDiscount;}
    public String getTransactionId(){ return transactionId;}


    public void setGlCashInHand(Integer glCashInHand){ this.glCashInHand = glCashInHand;}
    public void setGlBankAccount(Integer glBankAccount){ this.glBankAccount = glBankAccount;}
    public void setGlInterestIncome(Integer glInterestIncome){ this.glInterestIncome = glInterestIncome;}
    public void setGlDiscount(Integer glDiscount){ this.glDiscount = glDiscount;}
    public void setTransactionId(String transactionId){ this.transactionId = transactionId;}
    
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

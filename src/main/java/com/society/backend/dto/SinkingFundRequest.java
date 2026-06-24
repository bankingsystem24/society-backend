package com.society.backend.dto;

import jakarta.persistence.Column; 

public class SinkingFundRequest {

    private Long societyId;

    private String month;

    private int year;

    private Double amount;

    private Long createdBy;
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

    // ================= GETTERS & SETTERS =================

    public Long getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Long societyId) {
        this.societyId = societyId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}

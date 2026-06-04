package com.society.backend.gl.dto;

public class TrialBalanceDTO {

    private Integer glCode;
    private String accountName;
    private Double debit;
    private Double credit;

    // computed fields (not from DB)
    private Double balance;
    private String balanceType;

    // Default Constructor
    public TrialBalanceDTO() {
    }

    // ✅ JPQL Constructor (ONLY use this in query)
    public TrialBalanceDTO(Integer glCode,
                           String accountName,
                           Double debit,
                           Double credit) {
        this.glCode = glCode;
        this.accountName = accountName;
        this.debit = debit;
        this.credit = credit;
    }

    // Getters & Setters

    public Integer getGlCode() {
        return glCode;
    }

    public void setGlCode(Integer glCode) {
        this.glCode = glCode;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Double getDebit() {
        return debit;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }
}
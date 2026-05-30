package com.society.backend.gl.dto;

public class TrialBalanceDTO {

    private Integer glCode;
    private String accountName;
    private Double debit;
    private Double credit;
    private Double balance;
    private String accountType;
    private String balanceType;

    // Default Constructor
    public TrialBalanceDTO() {
    }

    // Constructor used by JPQL
    public TrialBalanceDTO(
            Integer glCode,
            String accountName,
            Double debit,
            Double credit
    ) {
        this.glCode = glCode;
        this.accountName = accountName;
        this.debit = debit;
        this.credit = credit;
        this.balance = (debit != null ? debit : 0.0)
                     - (credit != null ? credit : 0.0);
    }

    // Full Constructor
    public TrialBalanceDTO(
            Integer glCode,
            String accountName,
            Double debit,
            Double credit,
            Double balance,
            String accountType,
            String balanceType
    ) {
        this.glCode = glCode;
        this.accountName = accountName;
        this.debit = debit;
        this.credit = credit;
        this.balance = balance;
        this.accountType = accountType;
        this.balanceType = balanceType;
    }

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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }
}
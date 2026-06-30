package com.society.backend.gl.dto;

public class TrialBalanceDTO {

    private Integer glCode;
    private String accountName;

    // Opening
    private Double openingBalance;
    private String openingType;

    // Period Movement
    private Double debit;
    private Double credit;

    private Double openingDebit;
    private Double openingCredit;

    // Closing

    private Double closingDebit;
    private Double closingCredit;

    private Double closingBalance;
    private String closingType;

    private String accountType;
    private String groupName;

    // Default Constructor
    public TrialBalanceDTO() {
    }

    // Existing JPQL Constructor
public TrialBalanceDTO(
        Integer glCode,
        String accountName,
        Double openingDebit,
        Double openingCredit,
        Double debit,
        Double credit,
        String groupName) {

    this.glCode = glCode;
    this.accountName = accountName;
    this.openingDebit = openingDebit;
    this.openingCredit = openingCredit;
    this.debit = debit;
    this.credit = credit;
    this.groupName = groupName;
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

    public Double getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(Double openingBalance) {
        this.openingBalance = openingBalance;
    }

    public String getOpeningType() {
        return openingType;
    }

    public void setOpeningType(String openingType) {
        this.openingType = openingType;
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

    public Double getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(Double closingBalance) {
        this.closingBalance = closingBalance;
    }

    public String getClosingType() {
        return closingType;
    }

    public void setClosingType(String closingType) {
        this.closingType = closingType;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Double getOpeningDebit() {return openingDebit;}
    public void setOpeningDebit(Double openingDebit) {this.openingDebit = openingDebit;}
    public Double getOpeningCredit() {return openingCredit;}
    public void setopeningCredit(Double openingCredit) {this.openingCredit = openingCredit;}

    public Double getClosingDebit() {return closingDebit;}
    public void setClosingDebit(Double closingDebit) {this.closingDebit = closingDebit;}
    public Double getClosingCredit() {return closingCredit;}
    public void setClosingCredit(Double closingCredit) {this.closingCredit = closingCredit;}
}
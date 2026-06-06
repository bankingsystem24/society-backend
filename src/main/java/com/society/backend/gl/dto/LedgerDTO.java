package com.society.backend.gl.dto;

import java.time.LocalDate;

public class LedgerDTO {

    private LocalDate entryDate;
    private String voucherNo;
    private String voucherType;
    private String narration;
    private Double debitAmount;
    private Double creditAmount;
    private Double balance;
    private Integer glCode;
    private String accountHead;
    private String balanceType;

    public LedgerDTO() {
    }

    public LedgerDTO(
            LocalDate entryDate,
            String voucherNo,
            String voucherType,
            String narration,
            Double debitAmount,
            Double creditAmount,
            Double balance,
            Integer glCode,
            String accountHead,
            String balanceType
    ) {
        this.entryDate = entryDate;
        this.voucherNo = voucherNo;
        this.voucherType = voucherType;
        this.narration = narration;
        this.debitAmount = debitAmount;
        this.creditAmount = creditAmount;
        this.balance = balance;
        this.glCode = glCode;
        this.accountHead = accountHead;
        this.balanceType = balanceType;
    }

    // =========================
    // GETTERS & SETTERS
    // =========================

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public Double getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(Double debitAmount) {
        this.debitAmount = debitAmount;
    }

    public Double getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(Double creditAmount) {
        this.creditAmount = creditAmount;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Integer getGlCode() {
        return glCode;
    }

    public void setGlCode(Integer glCode) {
        this.glCode = glCode;
    }

    public String getAccountHead() {
        return accountHead;
    }

    public void setAccountHead(String accountHead) {
        this.accountHead = accountHead;
    }

    public String getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }
}
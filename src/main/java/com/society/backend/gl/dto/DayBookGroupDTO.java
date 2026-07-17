package com.society.backend.gl.dto;

import java.util.ArrayList;
import java.util.List;

public class DayBookGroupDTO {

    private Integer glCode;
    private String accountName;

    private Double totalDebit;
    private Double totalCredit;

    private List<DayBookDTO> transactions;

    public DayBookGroupDTO() {
        this.totalDebit = 0.0;
        this.totalCredit = 0.0;
        this.transactions = new ArrayList<>();
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

    public Double getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(Double totalDebit) {
        this.totalDebit = totalDebit;
    }

    public Double getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(Double totalCredit) {
        this.totalCredit = totalCredit;
    }

    public List<DayBookDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<DayBookDTO> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(DayBookDTO dto) {

        transactions.add(dto);

        totalDebit += dto.getDebitAmount() == null ? 0.0 : dto.getDebitAmount();

        totalCredit += dto.getCreditAmount() == null ? 0.0 : dto.getCreditAmount();

    }
}
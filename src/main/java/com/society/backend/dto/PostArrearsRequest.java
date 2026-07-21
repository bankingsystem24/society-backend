package com.society.backend.dto;

import java.time.LocalDate;

public class PostArrearsRequest {

    private Long societyId;
    private Long flatId;
    private Long financialYearId;
    private Double amount;
    private LocalDate dueDate;

    private String remarks;
    private Integer glReceivable;
    private Integer glCreditAccount;
    private Long createdBy;

    // Getters & Setters

    public Long getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Long societyId) {
        this.societyId = societyId;
    }

    public Long getFlatId() {
        return flatId;
    }

    public void setFlatId(Long flatId) {
        this.flatId = flatId;
    }

    public Long getFinancialYearId() {
        return financialYearId;
    }

    public void setFinancialYearId(Long financialYearId) {
        this.financialYearId = financialYearId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getGlReceivable(){return glReceivable;}
    public void setGlReceivable(Integer glReceivable){this.glReceivable = glReceivable;}

    public Integer getGlCreditAccount(){return glCreditAccount;}
    public void setGlCreditAccount(Integer glCreditAccount){this.glCreditAccount = glCreditAccount;}

    public Long getCreatedBy(){return createdBy;}
    public void setCreatedBy(Long createdBy){this.createdBy = createdBy;}
}

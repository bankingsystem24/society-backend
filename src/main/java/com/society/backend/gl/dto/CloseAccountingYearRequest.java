package com.society.backend.gl.dto;

public class CloseAccountingYearRequest {

    private Long accountingYearId;
    private Long societyId;
    private String username;

    public Long getAccountingYearId() { return accountingYearId; }
    public void setAccountingYearId(Long accountingYearId) { this.accountingYearId = accountingYearId; }
    public Long getSocietyId() { return societyId; }
    public void setSocietyId(Long societyId) { this.societyId = societyId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

}


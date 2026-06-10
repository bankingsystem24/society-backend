package com.society.backend.gl.dto;

public class GlOpeningBalanceRequest {
    private Long societyId;
    private Long financialYearId;
    private Integer glCode;
    private Double openingDebit;
    private Double openingCredit;
    private Double openingBalance;
    private Integer contraGlCode;

    public Long getSocietyId(){
        return societyId;
    }

    public void setSocietyId(Long societyId){
        this.societyId = societyId;
    }

    public Long getFinancialYearId(){
        return financialYearId;
    }    

    public void setFinancialYearId(Long financialYearId){
        this.financialYearId = financialYearId;
    }

    public Integer getGlCode() {
        return glCode;
    }

    public void setGlCode(Integer glCode) {
        this.glCode = glCode;
    }

    public Double getOpeningDebit() {
        return openingDebit;
    }

    public void setOpeningDebit(Double openingDebit) {
        this.openingDebit = openingDebit;
    }

    public Double getOpeningCredit() {
        return openingCredit;
    }

    public void setOpeningCredit(Double openingCredit) {
        this.openingCredit = openingCredit;
    }

    public Double getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(Double openingBalance) {
        this.openingBalance = openingBalance;
    }
    
    public Integer getContraGlCode() {
        return contraGlCode;
    }

    public void setContraGlCode(Integer contraGlCode) {
        this.contraGlCode = contraGlCode;
    }
}

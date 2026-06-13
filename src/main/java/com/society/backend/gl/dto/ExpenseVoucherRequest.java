package com.society.backend.gl.dto;

import jakarta.persistence.Column;

public class ExpenseVoucherRequest {

    private Long societyId;
    private String voucherDate;
    private Integer expenseGlCode;
    private Double amount;
    private String paymentMode;
    private String narration;
    private Long vendorId;
    @Column(name = "financial_year_id", nullable = false)
    private Long financialYearId;

    public Long getFinancialYearId() {
        return financialYearId;
    }

    public void setFinancialYearId(Long financialYearId) {
        this.financialYearId = financialYearId;
    }
    public Long getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Long societyId) {
        this.societyId = societyId;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public Integer getExpenseGlCode() {
        return expenseGlCode;
    }

    public void setExpenseGlCode(Integer expenseGlCode) {
        this.expenseGlCode = expenseGlCode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }
}
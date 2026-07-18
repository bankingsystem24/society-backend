package com.society.backend.gl.dto;

import java.time.LocalDate;

public class CashBookRequest {

    private Long societyId;
    private Long financialYearId;

    private LocalDate fromDate;
    private LocalDate toDate;

    // Cash / Bank GL Code
    private Integer accountCode;

    private String voucherType;
    private String paymentMode;

    private Long memberId;
    private Long flatId;

    private Boolean openingBalance = true;
    private Boolean showNarration = true;

    public CashBookRequest() {
    }

    public Long getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Long societyId) {
        this.societyId = societyId;
    }

    public Long getFinancialYearId() {
        return financialYearId;
    }

    public void setFinancialYearId(Long financialYearId) {
        this.financialYearId = financialYearId;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public Integer getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(Integer accountCode) {
        this.accountCode = accountCode;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getFlatId() {
        return flatId;
    }

    public void setFlatId(Long flatId) {
        this.flatId = flatId;
    }

    public Boolean getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(Boolean openingBalance) {
        this.openingBalance = openingBalance;
    }

    public Boolean getShowNarration() {
        return showNarration;
    }

    public void setShowNarration(Boolean showNarration) {
        this.showNarration = showNarration;
    }
}

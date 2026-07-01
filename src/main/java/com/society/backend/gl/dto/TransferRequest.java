package com.society.backend.gl.dto;

import java.time.LocalDate;

public class TransferRequest {

    private Long societyId;
    private Integer fromGlCode;
    private Integer toGlCode;
    private LocalDate voucherDate;
    private Double amount;
    private String narration;
    private Long financialYearId;

    public TransferRequest() {    }
    public Long getSocietyId() { return societyId; }
    public void setSocietyId(Long societyId) { this.societyId = societyId; }
    public Integer getFromGlCode() { return fromGlCode; }
    public void setFromGlCode(Integer fromGlCode) { this.fromGlCode = fromGlCode; }
    public Integer getToGlCode() { return toGlCode; }
    public void setToGlCode(Integer toGlCode) { this.toGlCode = toGlCode; }
    public LocalDate getVoucherDate() { return voucherDate; }
    public void setVoucherDate(LocalDate voucherDate) { this.voucherDate = voucherDate; }
    public Double getAmount() {  return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getNarration() { return narration; }
    public void setNarration(String narration) { this.narration = narration; }
    public Long getFinancialYearId() { return financialYearId; }
    public void setFinancialYearId(Long financialYearId) { this.financialYearId = financialYearId; }


}

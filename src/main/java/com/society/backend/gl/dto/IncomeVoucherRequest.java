package com.society.backend.gl.dto;
import java.time.LocalDate;

public class IncomeVoucherRequest {
    private String voucherNo;
    private LocalDate voucherDate;
    private Integer incomeGlCode;
    private Double amount;
    private String paymentMode;
    private String narration;
    private String receivedFrom;
    private Long journalId;
    private Long societyId;
    private Long financialYearId;
    private Integer glCashInHand;
    private Integer glBankAccount;
    public String getVoucherNo() { return voucherNo;}
    public void setVoucherNo(String voucherNo) {this.voucherNo = voucherNo;}
    public LocalDate getVoucherDate() { return voucherDate;}
    public void setVoucherDate(LocalDate voucherDate) {this.voucherDate = voucherDate;}
    public Integer getIncomeGlCode() { return incomeGlCode;}
    public void setIncomeGlCode(Integer incomeGlCode) { this.incomeGlCode = incomeGlCode;}
    public Double getAmount() { return amount;}
    public void setAmount(Double amount) { this.amount = amount;}
    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }
    public String getNarration() { return narration;}
    public void setNarration(String narration) {this.narration = narration;}
    public String getReceivedFrom() {return receivedFrom;}
    public void setReceivedFrom(String receivedFrom) {this.receivedFrom = receivedFrom;}
    public Long getJournalId() { return journalId; }
    public void setJournalId(Long journalId) { this.journalId = journalId; }
    public Long getSocietyId() { return societyId; }
    public void setSocietyId(Long societyId) { this.societyId = societyId;}
    public Long getFinancialYearId() { return financialYearId;}
    public void setFinancialYearId(Long financialYearId) {this.financialYearId = financialYearId;}
    public Integer getGlCashInHand(){ return glCashInHand;}
    public Integer getGlBankAccount(){ return glBankAccount;}
    public void setGlCashInHand(Integer glCashInHand){this.glCashInHand = glCashInHand;}
    public void setGlBabkAccount(Integer glBankAccount){this.glBankAccount = glBankAccount;}
}

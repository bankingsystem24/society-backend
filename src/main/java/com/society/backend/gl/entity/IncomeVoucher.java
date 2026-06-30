package com.society.backend.gl.entity;

import java.time.LocalDate;
import com.society.backend.entity.Society;
import jakarta.persistence.*;

@Entity
@Table(name = "income_voucher")
public class IncomeVoucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String voucherNo;
    private LocalDate voucherDate;
    private Integer incomeGlCode;
    private Double amount;
    private String paymentMode;
    private String narration;
    private String receivedFrom;
    private Long journalId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_id", nullable = false)
    private Society society;
    @Column(name = "financial_year_id", nullable = false)
    private Long financialYearId;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {this.id = id; }
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
    public Society getSociety() { return society;}
    public void setSociety(Society society) { this.society = society; }
    public Long getFinancialYearId() { return financialYearId;}
    public void setFinancialYearId(Long financialYearId) {this.financialYearId = financialYearId;}
}

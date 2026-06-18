package com.society.backend.gl.dto;
import java.time.LocalDate;

import jakarta.persistence.Column;
public class ContributionResponse {

    private Long id;
    private String name;
    private Double amount;
    private Double contributionAmount;
    private Double receiptAmount;
    private String mode;
    private LocalDate date;
    private LocalDate paidDate;
    private LocalDate dueDate;
    private String status;
    @Column(name = "flat_id", nullable = false)
    private Long flatId;
    private String flatNo;
    private String type;
    private Double areaSqFt;
    private Long societyId;
    private Long memberId;
    private String description;
    private String receiptNo;
    private String paymentMode;
    private String transactionId;
    @Column(name = "financial_year_id", nullable = false)
    private Long financialYearId;

    public Long getFlatId(){
        return flatId;
    }

    public void setFlatId(Long flatId){
        this.flatId = flatId;
    }

    public Long getFinancialYearId() {
        return financialYearId;
    }

    public void setFinancialYearId(Long financialYearId) {
        this.financialYearId = financialYearId;
    }

    // ---------------- GETTERS ----------------

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public Double getAmount(){
        return amount;
    }
    public Double getContributionAmount() {
        return contributionAmount;
    }

    public Double getReceiptAmount() {
        return receiptAmount;
    }

    public String getMode() {
        return mode;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDate getPaidDate() {
        return paidDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public Double getAreaSqFt() {
        return areaSqFt;
    }

    public Long getSocietyId() {
        return societyId;
    }

    public Long getMemberId() {
        return memberId;
    }

    // ---------------- SETTERS ----------------

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public void setContributionAmount(Double contributionAmount) {
        this.contributionAmount = contributionAmount;
    }

    public void setReceiptAmount(Double receiptAmount) {
        this.receiptAmount = receiptAmount;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setPaidDate(LocalDate paidDate) {
        this.paidDate = paidDate;
    }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public void setAreaSqFt(Double areaSqFt) {
        this.areaSqFt = areaSqFt;
    }

    public void setSocietyId(Long societyId) {
        this.societyId = societyId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getReceiptNo(){return receiptNo;}
    public void setReceiptNo(String receiptNo){this.receiptNo=receiptNo;}

    public String getPaymentMode(){return paymentMode;}
    public void setPaymentMode(String paymentMode){this.paymentMode=paymentMode;}

    public String getTransactionId(){return transactionId;}
    public void setTransactionId(String transactionId){this.transactionId=transactionId;}
}

package com.society.backend.dto;

import java.util.List;

import jakarta.persistence.Column;

public class ReceiptRequest {

    private String receiptNo;
    private Long societyId;
    private Long flatId;
    private Double maintenanceAmount;
    private Double interestAmount;
    private Double discountAmount;
    private Double totalAmount;
    private List<Long> billIds;
    private String paymentMode;
    private String transactionId;
    @Column(name = "financial_year_id", nullable = false)
    private Long financialYearId;

    public Long getFinancialYearId() {
        return financialYearId;
    }

    public void setFinancialYearId(Long financialYearId) {
        this.financialYearId = financialYearId;
    }    

    // ================= GETTERS =================

    public String getReceiptNo() {
        return receiptNo;
    }

    public Long getSocietyId() {
        return societyId;
    }

    public Long getFlatId() {
        return flatId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public List<Long> getBillIds() {
        return billIds;
    }

    // ================= SETTERS =================

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public void setSocietyId(Long societyId) {
        this.societyId = societyId;
    }

    public void setFlatId(Long flatId) {
        this.flatId = flatId;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setBillIds(List<Long> billIds) {
        this.billIds = billIds;
    }

    public Double getMaintenanceAmount() {
        return maintenanceAmount;
    }

    public void setMaintenanceAmount(Double maintenanceAmount) {
        this.maintenanceAmount = maintenanceAmount;
    }

    public Double getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(Double interestAmount) {
        this.interestAmount = interestAmount;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getPaymentMode(){
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode){
        this.paymentMode = paymentMode;
    }

    public String getTransactionId(){
        return transactionId;
    }

    public void setTransactionId(String transactionId){
        this.transactionId = transactionId;
    }
}


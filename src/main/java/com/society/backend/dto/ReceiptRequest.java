package com.society.backend.dto;

import java.util.List;

public class ReceiptRequest {

    private String receiptNo;
    private Long societyId;
    private Long flatId;
    private Double totalAmount;
    private List<Long> billIds;
    private String paymentMode;
    private String transactionId;

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


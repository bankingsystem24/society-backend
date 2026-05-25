package com.society.backend.dto;
import java.util.List;

public class PaymentRequest {

    private List<Long> billIds;
    private String paymentMode;
    private Long memberId;

    public List<Long> getBillIds() {
        return billIds;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId){
        this.memberId = memberId;
    }

    public void setBillIds(List<Long> billIds) {
        this.billIds = billIds;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }
}
package com.society.backend.dto;

import java.util.List;

public class VerifySinkingFundPaymentRequest {

    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;

    private List<Long> sinkingFundIds;

    private Long memberId;
    private Long userId;
    private Double amount;
    private String paymentMode;

    // getters setters
    public String getRazorpayOrderId() {
    return razorpayOrderId;
}

public void setRazorpayOrderId(String razorpayOrderId) {
    this.razorpayOrderId = razorpayOrderId;
}

public String getRazorpayPaymentId() {
    return razorpayPaymentId;
}

public void setRazorpayPaymentId(String razorpayPaymentId) {
    this.razorpayPaymentId = razorpayPaymentId;
}

public String getRazorpaySignature() {
    return razorpaySignature;
}

public void setRazorpaySignature(String razorpaySignature) {
    this.razorpaySignature = razorpaySignature;
}

public List<Long> getSinkingFundIds() {
    return sinkingFundIds;
}

public void setSinkingFundIds(List<Long> sinkingFundIds) {
    this.sinkingFundIds = sinkingFundIds;
}

public Long getMemberId() {
    return memberId;
}

public void setMemberId(Long memberId) {
    this.memberId = memberId;
}

public Long getUserId() {
    return userId;
}

public void setUserrId(Long userId) {
    this.userId = userId;
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


}

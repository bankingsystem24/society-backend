package com.society.backend.dto;

import java.util.List;

public class SinkingFundPaymentRequest {

    private List<Long> sinkingFundIds;
    private String paymentMode;

    public List<Long> getSinkingFundIds() {
        return sinkingFundIds;
    }

    public void setSinkingFundIds(List<Long> sinkingFundIds) {
        this.sinkingFundIds = sinkingFundIds;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }
}

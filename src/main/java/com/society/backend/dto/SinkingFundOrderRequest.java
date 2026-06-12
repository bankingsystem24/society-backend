package com.society.backend.dto;

import java.util.List;

public class SinkingFundOrderRequest {

    private List<Long> sinkingFundIds;
    private Long memberId;
    private Double amount;

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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}

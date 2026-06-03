package com.society.backend.gl.dto;

import com.society.backend.gl.enums.InterestType;
import com.society.backend.gl.enums.PenaltyType;

public class SocietyBillingPolicyDTO {

    private Long societyId;

    private Integer billingDay;

    private Integer graceDays;

    private Double interestRate;

    private InterestType interestType;

    private PenaltyType penaltyType;

    private Double penaltyValue;

    public SocietyBillingPolicyDTO() {
    }

    // ================= GETTERS & SETTERS =================

    public Long getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Long societyId) {
        this.societyId = societyId;
    }

    public Integer getBillingDay() {
        return billingDay;
    }

    public void setBillingDay(Integer billingDay) {
        this.billingDay = billingDay;
    }

    public Integer getGraceDays() {
        return graceDays;
    }

    public void setGraceDays(Integer graceDays) {
        this.graceDays = graceDays;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public InterestType getInterestType() {
        return interestType;
    }

    public void setInterestType(InterestType interestType) {
        this.interestType = interestType;
    }

    public PenaltyType getPenaltyType() {
        return penaltyType;
    }

    public void setPenaltyType(PenaltyType penaltyType) {
        this.penaltyType = penaltyType;
    }

    public Double getPenaltyValue() {
        return penaltyValue;
    }

    public void setPenaltyValue(Double penaltyValue) {
        this.penaltyValue = penaltyValue;
    }
}
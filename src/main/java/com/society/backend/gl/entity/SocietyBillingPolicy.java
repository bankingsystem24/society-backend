package com.society.backend.gl.entity;

import com.society.backend.entity.Society;
import com.society.backend.gl.enums.InterestType;
import com.society.backend.gl.enums.PenaltyType;

import jakarta.persistence.*;

@Entity
@Table(name = "society_billing_policy")
public class SocietyBillingPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_id", nullable = false, unique = true)
    private Society society;

    // Due date day of month (1-31)
    @Column(name = "billing_day", nullable = false)
    private Integer billingDay;

    // Grace period after due date
    @Column(name = "grace_days")
    private Integer graceDays = 0;

    // Interest rate
    @Column(name = "interest_rate")
    private Double interestRate = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(name = "interest_type")
    private InterestType interestType = InterestType.MONTHLY;

    @Enumerated(EnumType.STRING)
    @Column(name = "penalty_type")
    private PenaltyType penaltyType = PenaltyType.FIXED;

    // Fixed amount or percentage depending on penalty type
    @Column(name = "penalty_value")
    private Double penaltyValue = 0.0;

    @Column(name = "financial_year_id", nullable = false)
    private Long financialYearId;
    
    public Long getFinancialYearId() {
        return financialYearId;
    }

    public void setFinancialYearId(Long financialYearId) {
        this.financialYearId = financialYearId;
    }

    public SocietyBillingPolicy() {
    }

    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
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
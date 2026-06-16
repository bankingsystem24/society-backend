package com.society.backend.gl.entity;

import com.society.backend.entity.Society;
import com.society.backend.gl.enums.InterestType;

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

    // Interest rate
    @Column(name = "interest_rate")
    private Double interestRate = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(name = "interest_type")
    private InterestType interestType = InterestType.MONTHLY;

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

}
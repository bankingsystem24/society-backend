package com.society.backend.gl.entity;

import com.society.backend.entity.Society;
import jakarta.persistence.*;

@Entity
@Table(name = "gl_opening_balance")
public class GlOpeningBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // MANY opening balances belong to ONE society
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_id", nullable = false)
    private Society society;

    @Column(name = "financial_year_id", nullable = false)
    private Long financialYearId;

    @Column(name = "gl_code", nullable = false)
    private Integer glCode;

    @Column(name = "opening_debit")
    private Double openingDebit = 0.0;

    @Column(name = "opening_credit")
    private Double openingCredit = 0.0;

    @Column(name = "opening_balance")
    private Double openingBalance = 0.0;

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

    public Long getFinancialYearId() {
        return financialYearId;
    }

    public void setFinancialYearId(Long financialYearId) {
        this.financialYearId = financialYearId;
    }

    public Integer getGlCode() {
        return glCode;
    }

    public void setGlCode(Integer glCode) {
        this.glCode = glCode;
    }

    public Double getOpeningDebit() {
        return openingDebit;
    }

    public void setOpeningDebit(Double openingDebit) {
        this.openingDebit = openingDebit;
    }

    public Double getOpeningCredit() {
        return openingCredit;
    }

    public void setOpeningCredit(Double openingCredit) {
        this.openingCredit = openingCredit;
    }

    public Double getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(Double openingBalance) {
        this.openingBalance = openingBalance;
    }
}
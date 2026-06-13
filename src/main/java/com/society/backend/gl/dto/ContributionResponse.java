package com.society.backend.gl.dto;
import java.time.LocalDate;

import jakarta.persistence.Column;
public class ContributionResponse {

    private Long id;
    private String name;
    private Double amount;
    private String mode;
    private LocalDate date;
    private LocalDate dueDate;
    private String status;
    private String flatNo;
    private Double areaSqFt;
    private Long societyId;
    private Long memberId;
    @Column(name = "financial_year_id", nullable = false)
    private Long financialYearId;

    public Long getFinancialYearId() {
        return financialYearId;
    }

    public void setFinancialYearId(Long financialYearId) {
        this.financialYearId = financialYearId;
    }

    // ---------------- GETTERS ----------------

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }

    public String getMode() {
        return mode;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public Double getAreaSqFt() {
        return areaSqFt;
    }

    public Long getSocietyId() {
        return societyId;
    }

    public Long getMemberId() {
        return memberId;
    }

    // ---------------- SETTERS ----------------

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public void setAreaSqFt(Double areaSqFt) {
        this.areaSqFt = areaSqFt;
    }

    public void setSocietyId(Long societyId) {
        this.societyId = societyId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}

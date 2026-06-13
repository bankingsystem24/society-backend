package com.society.backend.dto;

import java.time.LocalDate;

import jakarta.persistence.Column;

public class BillingResponse {

    private Long id;
    private String month;
    private Integer year;

    private Double maintenanceAmount;
    private Double penaltyAmount;
    private Double totalAmount;

    private String status;

    private Long flatId;
    private String flatNo;

    private Long memberId;
    private String memberName;
    private Long receiptId;
    private String receiptNo;
    private Double interestAmount;
    private Double discountAmount;
    private LocalDate dueDate;
    @Column(name = "financial_year_id", nullable = false)
    private Long financialYearId;

    public Long getFinancialYearId() {
        return financialYearId;
    }

    public void setFinancialYearId(Long financialYearId) {
        this.financialYearId = financialYearId;
    }

    // ======================
    // Getters & Setters
    // ======================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getMaintenanceAmount() {
        return maintenanceAmount;
    }

    public void setMaintenanceAmount(Double maintenanceAmount) {
        this.maintenanceAmount = maintenanceAmount;
    }

    public Double getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(Double penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getFlatId() {
        return flatId;
    }

    public void setFlatId(Long flatId) {
        this.flatId = flatId;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
 
    public Long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public Double getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(Double interestAmount) {
        this.interestAmount = interestAmount;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

}
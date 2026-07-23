package com.society.backend.gl.dto;

import java.time.LocalDate;
import com.society.backend.enums.PaymentStatus;

public class DueBillsReport {

    private Long billId;
    private String flatNo;
    private String memberName;
    private String month;
    private Integer year;
    private Double maintenanceAmount;
    private Double penaltyAmount;
    private Double interestAmount;
    private Double discountAmount;
    private Double totalAmount;
    private LocalDate dueDate;
    private PaymentStatus status;

    public DueBillsReport() {
    }

    public DueBillsReport(
            Long billId,
            String flatNo,
            String memberName,
            String month,
            Integer year,
            Double maintenanceAmount,
            Double penaltyAmount,
            Double interestAmount,
            Double discountAmount,
            Double totalAmount,
            LocalDate dueDate,
            PaymentStatus status) {

        this.billId = billId;
        this.flatNo = flatNo;
        this.memberName = memberName;
        this.month = month;
        this.year = year;
        this.maintenanceAmount = maintenanceAmount;
        this.penaltyAmount = penaltyAmount;
        this.interestAmount = interestAmount;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.dueDate = dueDate;
        this.status = status;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
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

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

}

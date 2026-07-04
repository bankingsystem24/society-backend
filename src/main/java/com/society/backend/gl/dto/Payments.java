package com.society.backend.gl.dto;

import com.society.backend.entity.Society;
import com.society.backend.enums.PaymentStatus;

public class Payments {

    private String flatNo;
    private String description;
    private Double totalAmount;
    private Long societyId;
    private Long financialYearId;
    private PaymentStatus status; 

    public String getFlatNo(){return flatNo;}
    public void setFlatNo(String flatNo){this.flatNo = flatNo;}
    public String getDescription(){return description;}
    public void setDescription(String description){this.description = description;}
    public Double getTotalAmount(){return totalAmount;}
    public void setTotalAmount(Double totalAmount){this.totalAmount = totalAmount;}
    public Long getSocietyId() {return societyId;}
    public void setSocietyId(Long societyId) { this.societyId = societyId;}
    public Long getFinancialYearId() {return financialYearId;}
    public void setFinancialYearId(Long financialYearId) {this.financialYearId = financialYearId;}
    public PaymentStatus getStatus() {return status; }
    public void setStatus(PaymentStatus status) {this.status = status;}
}

package com.society.backend.dto;

import java.time.LocalDate;
import java.util.List;

public class InterestCalculationRequest {

    private List<Long> billIds;
    private LocalDate paymentDate;
    private Long financialYearId;
    private Integer selectedCount;

    public List<Long> getBillIds() {
        return billIds;
    }

    public void setBillIds(List<Long> billIds) {
        this.billIds = billIds;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Long getFinancialYearId(){return financialYearId;}
    public void setFinancialYearId(Long financialYearId){this.financialYearId = financialYearId;}

    public Integer getSelectedCount(){return selectedCount;}
    public void setSelectedCount(Integer selectedCount){ this.selectedCount = selectedCount;}
}

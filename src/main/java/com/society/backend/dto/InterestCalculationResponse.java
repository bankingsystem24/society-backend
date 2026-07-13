package com.society.backend.dto;

public class InterestCalculationResponse {

    private Double maintenanceAmount;
    private Double interestAmount;
    private Double discountAmount;
    private Double totalAmount;

    public InterestCalculationResponse() {
    }

    public InterestCalculationResponse(Double maintenanceAmount,
                                       Double interestAmount,
                                       Double discountAmount,
                                       Double totalAmount) {
        this.maintenanceAmount = maintenanceAmount;
        this.interestAmount = interestAmount;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
    }

    // Getters & Setters

    public Double getMaintenanceAmount(){return maintenanceAmount;}
    public void setMaintenanceAmount(Double maintenanceAmount){this.maintenanceAmount = maintenanceAmount;}

    public Double getInterestAmount(){return interestAmount;}
    public void setInterestAmount(Double interestAmount){this.interestAmount = interestAmount;}

    public Double getDiscountAmount(){return discountAmount;}
    public void setDiscountAmount(Double discountAmount){this.discountAmount = discountAmount;}

    public Double getTotalAmount(){return totalAmount;}
    public void setTotalAmount(Double totalAmount){this.totalAmount = totalAmount;}   


}

package com.society.backend.gl.dto;

import java.time.LocalDate;

import com.society.backend.gl.enums.BillType;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class ArrearsResponse {

    private Long id;
    private String flatNo;
    private String ownerName;
    private Double maintenanceAmount;
    private LocalDate dueDate;
    private String status;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillType billType = BillType.ARREARS;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Double getMaintenanceAmount() {
        return maintenanceAmount;
    }

    public void setMaintenanceAmount(Double maintenanceAmount) {
        this.maintenanceAmount = maintenanceAmount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

public BillType getBillType() {
    return billType;
}

public void setBillType(BillType billType) {
    this.billType = billType;
}
}

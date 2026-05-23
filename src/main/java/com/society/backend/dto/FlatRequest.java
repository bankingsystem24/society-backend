package com.society.backend.dto;

public class FlatRequest {

    private String flatNo;

    private String floorNo;

    private Double areaSqFt;

    private Integer bedrooms;

    private Double maintenanceAmount;

    private String status;

    private Boolean active;

    // =========================
    // RELATIONS
    // =========================
    private IdRequest society;

    private IdRequest wing;

    private IdRequest owner;

    // =========================
    // INNER ID CLASS
    // =========================
    public static class IdRequest {

        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    // =========================
    // GETTERS & SETTERS
    // =========================

    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public String getFloorNo() {
        return floorNo;
    }

    public void setFloorNo(String floorNo) {
        this.floorNo = floorNo;
    }

    public Double getAreaSqFt() {
        return areaSqFt;
    }

    public void setAreaSqFt(Double areaSqFt) {
        this.areaSqFt = areaSqFt;
    }

    public Integer getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(Integer bedrooms) {
        this.bedrooms = bedrooms;
    }

    public Double getMaintenanceAmount() {
        return maintenanceAmount;
    }

    public void setMaintenanceAmount(Double maintenanceAmount) {
        this.maintenanceAmount = maintenanceAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public IdRequest getSociety() {
        return society;
    }

    public void setSociety(IdRequest society) {
        this.society = society;
    }

    public IdRequest getWing() {
        return wing;
    }

    public void setWing(IdRequest wing) {
        this.wing = wing;
    }

    public IdRequest getOwner() {
        return owner;
    }

    public void setOwner(IdRequest owner) {
        this.owner = owner;
    }
}
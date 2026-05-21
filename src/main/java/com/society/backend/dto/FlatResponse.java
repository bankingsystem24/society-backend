package com.society.backend.dto;

public class FlatResponse {

    private Long id;
    private String flatNo;
    private String floorNo;
    private Double areaSqFt;
    private Integer bedrooms;
    private Double maintenanceAmount;
    private String status;

    private Long societyId;
    private String societyName;

    private Long wingId;
    private String wingName;

    private Long ownerId;
    private String ownerName;

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

public Long getSocietyId() {
    return societyId;
}

public void setSocietyId(Long societyId) {
    this.societyId = societyId;
}

public String getSocietyName() {
    return societyName;
}

public void setSocietyName(String societyName) {
    this.societyName = societyName;
}

public Long getWingId() {
    return wingId;
}

public void setWingId(Long wingId) {
    this.wingId = wingId;
}

public String getWingName() {
    return wingName;
}

public void setWingName(String wingName) {
    this.wingName = wingName;
}

public Long getOwnerId() {
    return ownerId;
}

public void setOwnerId(Long ownerId) {
    this.ownerId = ownerId;
}

public String getOwnerName() {
    return ownerName;
}

public void setOwnerName(String ownerName) {
    this.ownerName = ownerName;
}

}
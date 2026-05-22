package com.society.backend.dto;

import com.society.backend.entity.Member;
import com.society.backend.entity.Society;
import com.society.backend.entity.Wing;

public class FlatResponse {

    private Long id;
    private String flatNo;
    private String floorNo;
    private Double areaSqFt;
    private Integer bedrooms;
    private Double maintenanceAmount;
    private String status;

    private Society society;
    private Wing wing;
    private Member owner;
    

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

public Society getSociety() {
    return society;
}

public void setSociety(Society society){
    this.society = society; 
}

public Wing getWing(){
    return wing;
}

public void setWing(Wing wing){
    this.wing = wing;
}

public Member getOwner(){
    return owner;
}

public void setOwner(Member owner){
    this.owner = owner;
}

}
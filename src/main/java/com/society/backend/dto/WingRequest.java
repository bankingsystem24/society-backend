package com.society.backend.dto;

import com.society.backend.entity.Society;

public class WingRequest {

    private String wingName;
    private String description;
    private Boolean active;
    private Integer total_flats;
    private Integer total_floors;

    private Society society;

    public String getWingName() {
        return wingName;
    }

    public void setWingName(String wingName) {
        this.wingName = wingName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getTotal_flats() {
        return total_flats;
    }

    public void setTotal_flats(Integer total_flats) {
        this.total_flats = total_flats;
    }

    public Integer getTotal_floors() {
        return total_floors;
    }

    public void setTotal_floors(Integer total_floors) {
        this.total_floors = total_floors;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }
}
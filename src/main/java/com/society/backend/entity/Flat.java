package com.society.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "flats")
public class Flat extends SocietyBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String flatNo;

    private String floorNo;

    private Double areaSqFt;

    private Integer bedrooms;

    private Double maintenanceAmount;

    private String status; // "OCCUPIED" / "VACANT"

    private Boolean active = true;

    // =========================
    // Wing Relation
    // =========================
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "wing_id")
    private Wing wing;

    // =========================
    // Owner Relation
    // =========================
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "owner_member_id")
    private Member owner;

    // =========================
    // Society Relation
    // =========================
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "society_id")
    private Society society;

    // =========================
    // Getters & Setters
    // =========================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFlatNo() { return flatNo; }
    public void setFlatNo(String flatNo) { this.flatNo = flatNo; }

    public String getFloorNo() { return floorNo; }
    public void setFloorNo(String floorNo) { this.floorNo = floorNo; }

    public Double getAreaSqFt() { return areaSqFt; }
    public void setAreaSqFt(Double areaSqFt) { this.areaSqFt = areaSqFt; }

    public Integer getBedrooms() { return bedrooms; }
    public void setBedrooms(Integer bedrooms) { this.bedrooms = bedrooms; }

    public Double getMaintenanceAmount() { return maintenanceAmount; }
    public void setMaintenanceAmount(Double maintenanceAmount) { this.maintenanceAmount = maintenanceAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Wing getWing() { return wing; }
    public void setWing(Wing wing) { this.wing = wing; }

    public Member getOwner() { return owner; }
    public void setOwner(Member owner) { this.owner = owner; }

    public Society getSociety() { return society; }
    public void setSociety(Society society) { this.society = society; }
}
package com.society.backend.gl.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "penalty_policy")
@Data
public class PenaltyPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String penaltyType;
    private Double penaltyAmount;
    private Double penaltyPercentage;
    private String frequency;
    private Integer graceDays;
    private Double maxPenalty;
    private Boolean allowWaiver;
    private Boolean active;

    public Long getId() { return id;    }
    public void setId(Long id) {  this.id = id; }
    public String getPenaltyType() { return penaltyType; }
    public void setPenaltyType(String penaltyType) { this.penaltyType = penaltyType; }
    public Double getPenaltyAmount() { return penaltyAmount; }
    public void setPenaltyAmount(Double penaltyAmount) { this.penaltyAmount = penaltyAmount; }
    public Double getPenaltyPercentage() { return penaltyPercentage; }
    public void setPenaltyPercentage(Double penaltyPercentage) { this.penaltyPercentage = penaltyPercentage; }
    public String getFrequency() { return frequency;  }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public Integer getGraceDays() { return graceDays; }
    public void setGraceDays(Integer graceDays) { this.graceDays = graceDays; }
    public Double getMaxPenalty() {  return maxPenalty; }
    public void setMaxPenalty(Double maxPenalty) {  this.maxPenalty = maxPenalty; }
    public Boolean getAllowWaiver() { return allowWaiver; }
    public void setAllowWaiver(Boolean allowWaiver) {  this.allowWaiver = allowWaiver; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

}

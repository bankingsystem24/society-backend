package com.society.backend.gl.dto;
import java.math.BigDecimal;
import java.time.LocalDate;

public class DiscountPolicyRequest {

    private Long societyId;
    private String policyName;
    private Boolean active;
    private Integer daysBeforeDue;
    private BigDecimal discountPercent;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private String createdBy;

    public Long getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Long societyId) {
        this.societyId = societyId;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getcreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getDaysBeforeDue() {
        return daysBeforeDue;
    }

    public void setDaysBeforeDue(Integer daysBeforeDue) {
        this.daysBeforeDue = daysBeforeDue;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }
}

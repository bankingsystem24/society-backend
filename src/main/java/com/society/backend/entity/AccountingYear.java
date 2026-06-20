package com.society.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
    name = "accounting_year",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"society_id", "fy_code"})
    }
)
public class AccountingYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_id", nullable = false)
    private Society society;

    // Example: 2025-26
    @Column(name = "fy_code", nullable = false, length = 10)
    private String fyCode;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = false;

    // Optional: who created it
    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private LocalDate createdAt;

    // ================= GETTERS / SETTERS =================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public String getFyCode() {
        return fyCode;
    }

    public void setFyCode(String fyCode) {
        this.fyCode = fyCode;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

}

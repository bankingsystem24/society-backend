package com.society.backend.gl.entity;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "financial_year")
public class Dummy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String yearName;

    private LocalDate fromDate;

    private LocalDate toDate;

    private Boolean active = true;

    private Long societyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYearName() {
        return yearName;
    }

    public void setYearName(String yearName) {
        this.yearName = yearName;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Long societyId) {
        this.societyId = societyId;
    }
}
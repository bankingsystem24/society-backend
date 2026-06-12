package com.society.backend.dto;

import java.time.LocalDate;

public class CompulsoryContributionRequest {

    private String type;        // COMPULSORY
    private String name;

    private LocalDate date;
    private LocalDate dueDate;

    private String mode;        // FLAT / AREA

    private Double flatAmount;
    private Double rate;

    private String description;

    private Double minAmount;
    private Long userId;

    // ---------------- GETTERS ----------------

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getMode() {
        return mode;
    }

    public Double getFlatAmount() {
        return flatAmount;
    }

    public Double getRate() {
        return rate;
    }

    public String getDescription() {
        return description;
    }

    public Double getMinAmount() {
        return minAmount;
    }

    // ---------------- SETTERS ----------------

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setFlatAmount(Double flatAmount) {
        this.flatAmount = flatAmount;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMinAmount(Double minAmount) {
        this.minAmount = minAmount;
    }
}

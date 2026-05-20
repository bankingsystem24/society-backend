package com.society.backend.entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class SocietyBaseEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "society_id")
    private Society society;

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }
}
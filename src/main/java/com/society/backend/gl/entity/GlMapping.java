package com.society.backend.gl.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "gl_mapping")
public class GlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private Long societyId;
    private Integer gl_receivable;
    private Integer gl_credit_account;

    public GlMapping() {
    }

    public GlMapping(Long id, String description, Long societyId,
                     Integer gl_receivable, Integer gl_credit_account) {
        this.id = id;
        this.description = description;
        this.societyId = societyId;
        this.gl_receivable = gl_receivable;
        this.gl_credit_account = gl_credit_account;
    }

    public Long getId() { return id;    }
    public void setId(Long id) {this.id = id; }
    public String getDescription() { return description;  }
    public void setDescription(String description) { this.description = description;  }
    public Long getSocietyId() { return societyId; }
    public void setSocietyId(Long societyId) { this.societyId = societyId; }
    public Integer getGl_receivable() { return gl_receivable;  }
    public void setGl_receivable(Integer gl_receivable) { this.gl_receivable = gl_receivable; }
    public Integer getGl_credit_account() { return gl_credit_account; }
    public void setGl_credit_account(Integer gl_credit_account) { this.gl_credit_account = gl_credit_account;}

}
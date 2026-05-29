package com.society.backend.gl.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "gl_master")
public class GlMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gl_code", unique = true, nullable = false)
    private Integer glCode;

    @Column(name = "account_name", nullable = false, length = 200)
    private String accountName;

    @Column(name = "group_name", nullable = false, length = 100)
    private String groupName;

    @Column(name = "account_type", nullable = false, length = 50)
    private String accountType;
    // ASSET, LIABILITY, INCOME, EXPENSE, EQUITY

    @Column(name = "parent_gl_code")
    private Integer parentGlCode;
    // For hierarchy/grouping

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "society_id", nullable = false)
    private Long societyId;

    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGlCode() {
        return glCode;
    }

    public void setGlCode(Integer glCode) {
        this.glCode = glCode;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Integer getParentGlCode() {
        return parentGlCode;
    }

    public void setParentGlCode(Integer parentGlCode) {
        this.parentGlCode = parentGlCode;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Long societyId) {
        this.societyId = societyId;
    }
}
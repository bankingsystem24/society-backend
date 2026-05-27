package com.society.backend.gl.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "gl_master")
public class GlMaster {

    @Id
    @Column(name = "gl_code")
    private Integer glCode;

    @Column(name = "group_name", nullable = false)
    private String groupName; // ASSETS, LIABILITIES, INCOME, EXPENSES, RESERVES

    @Column(name = "account_name", nullable = false)
    private String accountName;

    // ================= GETTERS & SETTERS =================

    public Integer getGlCode() {
        return glCode;
    }

    public void setGlCode(Integer glCode) {
        this.glCode = glCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
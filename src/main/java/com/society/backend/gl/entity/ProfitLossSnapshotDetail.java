package com.society.backend.gl.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "profit_loss_snapshot_detail")
public class ProfitLossSnapshotDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "header_id", nullable = false)
    private ProfitLossSnapshotHeader header;

    @Column(name = "gl_code", nullable = false)
    private Integer glCode;

    @Column(name = "account_name", nullable = false, length = 200)
    private String accountName;

    @Column(name = "account_type", nullable = false, length = 20)
    private String accountType;

    @Column(name = "amount", nullable = false)
    private Double amount;

    public ProfitLossSnapshotDetail() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProfitLossSnapshotHeader getHeader() {
        return header;
    }

    public void setHeader(ProfitLossSnapshotHeader header) {
        this.header = header;
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
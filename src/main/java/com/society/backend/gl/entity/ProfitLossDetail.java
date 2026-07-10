package com.society.backend.gl.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "profit_loss_detail")
public class ProfitLossDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "header_id")
    private ProfitLossHeader header;

    @Column(name = "gl_code")
    private Integer glCode;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "amount")
    private Double amount;

    public ProfitLossDetail() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProfitLossHeader getHeader() {
        return header;
    }

    public void setHeader(ProfitLossHeader header) {
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
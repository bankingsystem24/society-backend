package com.society.backend.gl.dto;

import java.time.LocalDate;

public class DayBookDTO {

    private LocalDate entryDate;
    private String voucherNo;

    private Integer glCode;
    private String accountName;

    private Long flatId;
    private Long ownerMemberId;
    private String memberName;

    private Double debitAmount;
    private Double creditAmount;

    private String remarks;
    private String entityType;

    public DayBookDTO() {
    }

    public DayBookDTO(LocalDate entryDate,
                      String voucherNo,
                      Integer glCode,
                      String accountName,
                      Long flatId,
                      Long ownerMemberId,
                      String memberName,
                      Double debitAmount,
                      Double creditAmount,
                      String remarks,
                      String entityType) {
        this.entryDate = entryDate;
        this.voucherNo = voucherNo;
        this.glCode = glCode;
        this.accountName = accountName;
        this.flatId = flatId;
        this.ownerMemberId = ownerMemberId;
        this.memberName = memberName;
        this.debitAmount = debitAmount;
        this.creditAmount = creditAmount;
        this.remarks = remarks;
        this.entityType = entityType;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
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

    public Long getFlatId() {
        return flatId;
    }

    public void setFlatId(Long flatId) {
        this.flatId = flatId;
    }

    public Long getOwnerMemberId() {
        return ownerMemberId;
    }

    public void setOwnerMemberId(Long ownerMemberId) {
        this.ownerMemberId = ownerMemberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Double getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(Double debitAmount) {
        this.debitAmount = debitAmount;
    }

    public Double getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(Double creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
}
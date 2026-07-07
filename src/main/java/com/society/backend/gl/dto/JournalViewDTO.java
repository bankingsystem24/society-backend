package com.society.backend.gl.dto;

import java.time.LocalDate;

public class JournalViewDTO {

    private Long journalId;

    private String voucherNo;

    private String voucherType;

    private LocalDate entryDate;

    private String narration;

    private Integer glCode;

    private String accountHead;

    private Double debitAmount;

    private Double creditAmount;

    private String entityType;

    private Long entityId;
    private Long memberId;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public JournalViewDTO(
            Long journalId,
            String voucherNo,
            String voucherType,
            LocalDate entryDate,
            String narration,
            Integer glCode,
            String accountHead,
            Double debitAmount,
            Double creditAmount,
            String entityType,
            Long entityId,
            Long memberId
    ) {

        this.journalId = journalId;
        this.voucherNo = voucherNo;
        this.voucherType = voucherType;
        this.entryDate = entryDate;
        this.narration = narration;
        this.glCode = glCode;
        this.accountHead = accountHead;
        this.debitAmount = debitAmount;
        this.creditAmount = creditAmount;
        this.entityType = entityType;
        this.entityId = entityId;
        this.memberId = memberId;
    }

    // =====================================================
    // GETTERS & SETTERS
    // =====================================================

    public Long getJournalId() {
        return journalId;
    }

    public void setJournalId(Long journalId) {
        this.journalId = journalId;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public Integer getGlCode() {
        return glCode;
    }

    public void setGlCode(Integer glCode) {
        this.glCode = glCode;
    }

    public String getAccountHead() {
        return accountHead;
    }

    public void setAccountHead(String accountHead) {
        this.accountHead = accountHead;
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

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getMemberId(){return memberId;}
    public void setMemberId(Long memberId){this.memberId = memberId;}
}
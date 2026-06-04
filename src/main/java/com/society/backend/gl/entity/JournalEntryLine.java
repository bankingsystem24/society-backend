package com.society.backend.gl.entity;

import com.society.backend.entity.Member;

import jakarta.persistence.*;

@Entity
@Table(name = "journal_entry_line")
public class JournalEntryLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "journal_id", nullable = false)
    private Long journalId;

    @Column(name = "line_no")
    private Integer lineNo;

    @Column(name = "gl_code", nullable = false)
    private Integer glCode;

    @Column(name = "debit_amount")
    private Double debitAmount = 0.0;

    @Column(name = "credit_amount")
    private Double creditAmount = 0.0;

    @Column(name = "entity_type", length = 50)
    private String entityType;
    // MEMBER / VENDOR / BANK / EMPLOYEE

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "remarks", length = 300)
    private String remarks;

    @Column(name = "society_id", nullable = false)
    private Long societyId;

    @Column(name = "flat_id")
    private Long flatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJournalId() {
        return journalId;
    }

    public void setJournalId(Long journalId) {
        this.journalId = journalId;
    }

    public Integer getLineNo() {
        return lineNo;
    }

    public void setLineNo(Integer lineNo) {
        this.lineNo = lineNo;
    }

    public Integer getGlCode() {
        return glCode;
    }

    public void setGlCode(Integer glCode) {
        this.glCode = glCode;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Long societyId) {
        this.societyId = societyId;
    }

    public Long getFlatId() {
        return flatId;
    }

    public void setFlatId(Long flatId) {
        this.flatId = flatId;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
package com.society.backend.gl.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.society.backend.entity.Society;

@Entity
@Table(name = "transfer_voucher")
public class TransferVoucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_id", nullable = false)
    private Society society;
    @Column(name = "voucher_no")
    private String voucherNo;
    @Column(name = "voucher_date", nullable = false)
    private LocalDate voucherDate;
    @Column(name = "from_gl_code", nullable = false)
    private Integer fromGlCode;
    @Column(name = "to_gl_code", nullable = false)
    private Integer toGlCode;
    @Column(name = "amount", nullable = false)
    private Double amount;
    @Column(name = "narration", length = 500)
    private String narration;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "financial_year_id", nullable = false)
    private Long financialYearId;
    @Column(name = "journal_id")
    private Long journalId;
    public TransferVoucher() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id;  }
    public Society getSociety() { return society; }
    public void setSociety(Society society) { this.society = society; }
    public String getVoucherNo() { return voucherNo; }
    public void setVoucherNo(String voucherNo) { this.voucherNo = voucherNo;  }
    public LocalDate getVoucherDate() { return voucherDate;  }
    public void setVoucherDate(LocalDate voucherDate) { this.voucherDate = voucherDate;  }
    public Integer getFromGlCode() { return fromGlCode;    }
    public void setFromGlCode(Integer fromGlCode) { this.fromGlCode = fromGlCode;  }
    public Integer getToGlCode() { return toGlCode;    }
    public void setToGlCode(Integer toGlCode) { this.toGlCode = toGlCode;  }
    public Double getAmount() { return amount;   }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getNarration() { return narration;   }
    public void setNarration(String narration) { this.narration = narration; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) {  this.createdAt = createdAt; }
    public Long getFinancialYearId() { return financialYearId; }
    public void setFinancialYearId(Long financialYearId) { this.financialYearId = financialYearId; }
    public Long getJournalId() { return journalId; }
    public void setJournalId(Long journalId) { this.journalId = journalId; }
}

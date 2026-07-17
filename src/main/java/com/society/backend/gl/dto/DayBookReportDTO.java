package com.society.backend.gl.dto;

import java.util.ArrayList;
import java.util.List;

public class DayBookReportDTO {

    private List<DayBookGroupDTO> debitGroups = new ArrayList<>();

    private List<DayBookGroupDTO> creditGroups = new ArrayList<>();

    private Double totalDebit = 0.0;

    private Double totalCredit = 0.0;

    public DayBookReportDTO() {
    }

    public List<DayBookGroupDTO> getDebitGroups() {
        return debitGroups;
    }

    public void setDebitGroups(List<DayBookGroupDTO> debitGroups) {
        this.debitGroups = debitGroups;
    }

    public List<DayBookGroupDTO> getCreditGroups() {
        return creditGroups;
    }

    public void setCreditGroups(List<DayBookGroupDTO> creditGroups) {
        this.creditGroups = creditGroups;
    }

    public Double getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(Double totalDebit) {
        this.totalDebit = totalDebit;
    }

    public Double getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(Double totalCredit) {
        this.totalCredit = totalCredit;
    }
}
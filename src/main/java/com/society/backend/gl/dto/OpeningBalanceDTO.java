package com.society.backend.gl.dto;

public class OpeningBalanceDTO {

    private Integer glCode;
    private String accountName;
    private Double openingBalance;

    public OpeningBalanceDTO(Integer glCode, String accountName, Double openingBalance) {
        this.glCode = glCode;
        this.accountName = accountName;
        this.openingBalance = openingBalance;
    }

    public Integer getGlCode() {
        return glCode;
    }

    public String getAccountName() {
        return accountName;
    }

    public Double getOpeningBalance() {
        return openingBalance;
    }
}

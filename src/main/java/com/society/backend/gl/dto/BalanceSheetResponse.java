package com.society.backend.gl.dto;
import java.util.List;

public class BalanceSheetResponse {

    private List<BalanceSheetRow> assets;
    private List<BalanceSheetRow> liabilities;
    private List<BalanceSheetRow> equity;

    private Double totalAssets = 0.0;
    private Double totalLiabilities = 0.0;
    private Double totalEquity = 0.0;

    public List<BalanceSheetRow> getAssets() {
        return assets;
    }

    public void setAssets(List<BalanceSheetRow> assets) {
        this.assets = assets;
    }

    public List<BalanceSheetRow> getLiabilities() {
        return liabilities;
    }

    public void setLiabilities(List<BalanceSheetRow> liabilities) {
        this.liabilities = liabilities;
    }

    public List<BalanceSheetRow> getEquity() {
        return equity;
    }

    public void setEquity(List<BalanceSheetRow> equity) {
        this.equity = equity;
    }

    public Double getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(Double totalAssets) {
        this.totalAssets = totalAssets;
    }

    public Double getTotalLiabilities() {
        return totalLiabilities;
    }

    public void setTotalLiabilities(Double totalLiabilities) {
        this.totalLiabilities = totalLiabilities;
    }

    public Double getTotalEquity() {
        return totalEquity;
    }

    public void setTotalEquity(Double totalEquity) {
        this.totalEquity = totalEquity;
    }
}

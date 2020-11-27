package com.csci571.aditya.stockapp.localstorage;

public class FavoriteStorageModel {
    private String stockTicker;
    private String companyName;

    public FavoriteStorageModel(String stockTicker, String companyName) {
        this.stockTicker = stockTicker;
        this.companyName = companyName;
    }

    public String getStockTicker() {
        return stockTicker;
    }

    public void setStockTicker(String stockTicker) {
        this.stockTicker = stockTicker;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "FavoriteStorageModel{" +
                "stockTicker='" + stockTicker + '\'' +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}

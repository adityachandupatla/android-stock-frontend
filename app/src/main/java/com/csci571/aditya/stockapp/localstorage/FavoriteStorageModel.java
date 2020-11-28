package com.csci571.aditya.stockapp.localstorage;

public class FavoriteStorageModel {
    private String stockTicker;
    private String companyName;
    private double lastPrice;

    public FavoriteStorageModel(String stockTicker, String companyName, double lastPrice) {
        this.stockTicker = stockTicker;
        this.companyName = companyName;
        this.lastPrice = lastPrice;
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

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    @Override
    public String toString() {
        return "FavoriteStorageModel{" +
                "stockTicker='" + stockTicker + '\'' +
                ", companyName='" + companyName + '\'' +
                ", lastPrice=" + lastPrice +
                '}';
    }
}

package com.csci571.aditya.stockapp.localstorage;

public class FavoriteStorageModel {
    private String stockTicker;
    private String companyName;
    private double lastPrice;
    private double stockPrice;

    public FavoriteStorageModel(String stockTicker, String companyName, double lastPrice, double stockPrice) {
        this.stockTicker = stockTicker;
        this.companyName = companyName;
        this.lastPrice = lastPrice;
        this.stockPrice = stockPrice;
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

    public double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }

    @Override
    public String toString() {
        return "FavoriteStorageModel{" +
                "stockTicker='" + stockTicker + '\'' +
                ", companyName='" + companyName + '\'' +
                ", lastPrice=" + lastPrice +
                ", stockPrice=" + stockPrice +
                '}';
    }
}

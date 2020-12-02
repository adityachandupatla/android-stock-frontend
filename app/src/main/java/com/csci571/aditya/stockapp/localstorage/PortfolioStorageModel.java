package com.csci571.aditya.stockapp.localstorage;

public class PortfolioStorageModel {
    private String stockTicker;
    private double sharesOwned;
    private double totalAmount;
    private double stockPrice;

    public PortfolioStorageModel(String stockTicker, double sharesOwned, double totalAmount, double stockPrice) {
        this.stockTicker = stockTicker;
        this.sharesOwned = sharesOwned;
        this.totalAmount = totalAmount;
        this.stockPrice = stockPrice;
    }

    public String getStockTicker() {
        return stockTicker;
    }

    public void setStockTicker(String stockTicker) {
        this.stockTicker = stockTicker;
    }

    public double getSharesOwned() {
        return sharesOwned;
    }

    public void setSharesOwned(double sharesOwned) {
        this.sharesOwned = sharesOwned;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }

    @Override
    public String toString() {
        return "PortfolioStorageModel{" +
                "stockTicker='" + stockTicker + '\'' +
                ", sharesOwned=" + sharesOwned +
                ", totalAmount=" + totalAmount +
                ", stockPrice=" + stockPrice +
                '}';
    }
}

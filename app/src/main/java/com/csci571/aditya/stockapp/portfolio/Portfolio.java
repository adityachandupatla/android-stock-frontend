package com.csci571.aditya.stockapp.portfolio;

import com.csci571.aditya.stockapp.R;
import com.csci571.aditya.stockapp.utils.Change;

import androidx.annotation.DrawableRes;

public class Portfolio {
    private String ticker;
    private double shares;
    private double stockPrice;
    private double changePercentage;
    private Change change;
    private double totalAmountOwned;

    @DrawableRes
    private int detailArrowImage;
    @DrawableRes
    private int changeImage;

    public Portfolio(String ticker, double shares, double totalAmountOwned, double stockPrice) {
        this.ticker = ticker;
        this.shares = shares;
        this.totalAmountOwned = totalAmountOwned;
        this.stockPrice = stockPrice;

        this.changePercentage = stockPrice - (totalAmountOwned / shares);
        updateChangeImage();

        this.detailArrowImage = R.drawable.ic_baseline_keyboard_arrow_right_24;
    }

    private void updateChangeImage() {
        if (changePercentage > 0) {
            this.change = Change.INCREASE;
            this.changeImage = R.drawable.ic_twotone_trending_up_24;
        }
        else if (changePercentage < 0) {
            this.change = Change.DECREASE;
            this.changeImage = R.drawable.ic_baseline_trending_down_24;
        }
        else {
            this.change = Change.SAME;
            this.changeImage = -1;
        }
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public double getShares() {
        return shares;
    }

    public void setShares(double shares) {
        this.shares = shares;
    }

    public double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }

    public double getChangePercentage() {
        return changePercentage;
    }

    public void setChangePercentage(double changePercentage) {
        this.changePercentage = changePercentage;
        updateChangeImage();
    }

    public Change getChange() {
        return change;
    }

    public void setChange(Change change) {
        this.change = change;
    }

    public int getDetailArrowImage() {
        return detailArrowImage;
    }

    public void setDetailArrowImage(int detailArrowImage) {
        this.detailArrowImage = detailArrowImage;
    }

    public int getChangeImage() {
        return changeImage;
    }

    public void setChangeImage(int changeImage) {
        this.changeImage = changeImage;
    }

    public double getTotalAmountOwned() {
        return totalAmountOwned;
    }

    public void setTotalAmountOwned(double totalAmountOwned) {
        this.totalAmountOwned = totalAmountOwned;
    }

    @Override
    public String toString() {
        return "Portfolio{" +
                "ticker='" + ticker + '\'' +
                ", shares=" + shares +
                ", stockPrice=" + stockPrice +
                ", changePercentage=" + changePercentage +
                ", change=" + change +
                ", totalAmountOwned=" + totalAmountOwned +
                ", detailArrowImage=" + detailArrowImage +
                ", changeImage=" + changeImage +
                '}';
    }
}

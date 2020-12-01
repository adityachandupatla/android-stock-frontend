package com.csci571.aditya.stockapp.favorite;

import com.csci571.aditya.stockapp.R;
import com.csci571.aditya.stockapp.utils.Change;

import androidx.annotation.DrawableRes;

public class Favorite {
    private String ticker;
    private double shares;
    private String companyName;
    private double stockPrice;
    private double changePercentage;
    private Change change;
    private double lastPrice;

    @DrawableRes
    private int detailArrowImage;
    @DrawableRes
    private int changeImage;


    public Favorite(String ticker, double shares, String companyName, double lastPrice) {
        this.ticker = ticker;
        this.shares = shares;
        this.companyName = companyName;
        this.lastPrice = lastPrice;

        this.detailArrowImage = R.drawable.ic_baseline_keyboard_arrow_right_24;

        // These are default values
        this.stockPrice = 0;
        this.changePercentage = 0;
        this.change = Change.SAME;
        this.changeImage = -1;
    }

    public void updateChangeImage() {
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
        return "Favorite{" +
                "ticker='" + ticker + '\'' +
                ", shares=" + shares +
                ", companyName='" + companyName + '\'' +
                ", stockPrice=" + stockPrice +
                ", changePercentage=" + changePercentage +
                ", change=" + change +
                ", lastPrice=" + lastPrice +
                ", detailArrowImage=" + detailArrowImage +
                ", changeImage=" + changeImage +
                '}';
    }
}

package com.csci571.aditya.stockapp.models;

import java.util.Map;

public class SummaryModel {
    private boolean success;
    private String stockTickerSymbol;
    private String timestamp;
    private double lastPrice;
    private double previousClosingPrice;
    private double openingPrice;
    private double highPrice;
    private double lowPrice;
    private double volume;
    private double bidSize;
    private double bidPrice;
    private double askSize;
    private double askPrice;
    private double midPrice;
    private Map<String, Object> sampleEndpoints;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getStockTickerSymbol() {
        return stockTickerSymbol;
    }

    public void setStockTickerSymbol(String stockTickerSymbol) {
        this.stockTickerSymbol = stockTickerSymbol;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public double getPreviousClosingPrice() {
        return previousClosingPrice;
    }

    public void setPreviousClosingPrice(double previousClosingPrice) {
        this.previousClosingPrice = previousClosingPrice;
    }

    public double getOpeningPrice() {
        return openingPrice;
    }

    public void setOpeningPrice(double openingPrice) {
        this.openingPrice = openingPrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getBidSize() {
        return bidSize;
    }

    public void setBidSize(double bidSize) {
        this.bidSize = bidSize;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public double getAskSize() {
        return askSize;
    }

    public void setAskSize(double askSize) {
        this.askSize = askSize;
    }

    public double getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(double askPrice) {
        this.askPrice = askPrice;
    }

    public double getMidPrice() {
        return midPrice;
    }

    public void setMidPrice(double midPrice) {
        this.midPrice = midPrice;
    }

    public Map<String, Object> getSampleEndpoints() {
        return sampleEndpoints;
    }

    public void setSampleEndpoints(Map<String, Object> sampleEndpoints) {
        this.sampleEndpoints = sampleEndpoints;
    }
}

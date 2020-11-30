package com.csci571.aditya.stockapp.models;

import java.util.Map;

public class OutlookModel {
    private boolean success;
    private String companyName;
    private String stockTickerSymbol;
    private String stockExchangeCode;
    private String companyStartDate;
    private String description;
    private Map<String, String> sampleEndpoints;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStockTickerSymbol() {
        return stockTickerSymbol;
    }

    public void setStockTickerSymbol(String stockTickerSymbol) {
        this.stockTickerSymbol = stockTickerSymbol;
    }

    public String getStockExchangeCode() {
        return stockExchangeCode;
    }

    public void setStockExchangeCode(String stockExchangeCode) {
        this.stockExchangeCode = stockExchangeCode;
    }

    public String getCompanyStartDate() {
        return companyStartDate;
    }

    public void setCompanyStartDate(String companyStartDate) {
        this.companyStartDate = companyStartDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getSampleEndpoints() {
        return sampleEndpoints;
    }

    public void setSampleEndpoints(Map<String, String> sampleEndpoints) {
        this.sampleEndpoints = sampleEndpoints;
    }
}

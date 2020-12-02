package com.csci571.aditya.stockapp.models;

import java.util.ArrayList;
import java.util.Map;

public class SummaryWrapperModel {
    private boolean success;
    private ArrayList<SummaryModel> data;
    private Map<String, String> sampleEndpoints;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<SummaryModel> getData() {
        return data;
    }

    public void setData(ArrayList<SummaryModel> data) {
        this.data = data;
    }

    public Map<String, String> getSampleEndpoints() {
        return sampleEndpoints;
    }

    public void setSampleEndpoints(Map<String, String> sampleEndpoints) {
        this.sampleEndpoints = sampleEndpoints;
    }
}

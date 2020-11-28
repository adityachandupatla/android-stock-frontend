package com.csci571.aditya.stockapp.models;

import java.util.ArrayList;
import java.util.Map;

public class AutoSuggestModel {
    private boolean success;
    private ArrayList<Suggestion> data;
    private Map<String, Object> sampleEndpoints;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<Suggestion> getData() {
        return data;
    }

    public void setData(ArrayList<Suggestion> data) {
        this.data = data;
    }

    public Map<String, Object> getSampleEndpoints() {
        return sampleEndpoints;
    }

    public void setSampleEndpoints(Map<String, Object> sampleEndpoints) {
        this.sampleEndpoints = sampleEndpoints;
    }
}

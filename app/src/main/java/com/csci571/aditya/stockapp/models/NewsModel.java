package com.csci571.aditya.stockapp.models;

import java.util.List;
import java.util.Map;

public class NewsModel {
    private boolean success;
    private List<ArticleModel> articles;
    private Map<String, String> sampleEndpoints;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<ArticleModel> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleModel> articles) {
        this.articles = articles;
    }

    public Map<String, String> getSampleEndpoints() {
        return sampleEndpoints;
    }

    public void setSampleEndpoints(Map<String, String> sampleEndpoints) {
        this.sampleEndpoints = sampleEndpoints;
    }
}

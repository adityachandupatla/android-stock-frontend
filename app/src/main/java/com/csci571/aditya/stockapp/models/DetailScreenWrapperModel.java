package com.csci571.aditya.stockapp.models;

public class DetailScreenWrapperModel {
    private OutlookModel outlookModel;
    private SummaryModel summaryModel;
    private NewsModel newsModel;

    public OutlookModel getOutlookModel() {
        return outlookModel;
    }

    public void setOutlookModel(OutlookModel outlookModel) {
        this.outlookModel = outlookModel;
    }

    public SummaryModel getSummaryModel() {
        return summaryModel;
    }

    public void setSummaryModel(SummaryModel summaryModel) {
        this.summaryModel = summaryModel;
    }

    public NewsModel getNewsModel() {
        return newsModel;
    }

    public void setNewsModel(NewsModel newsModel) {
        this.newsModel = newsModel;
    }
}

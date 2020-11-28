package com.csci571.aditya.stockapp.network;

import android.content.Context;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashSet;

import androidx.recyclerview.widget.RecyclerView;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class HomeScreenService implements Runnable {

    private HashSet<String> tickerSet;
    private ProgressBar progressBar;
    private TextView loadingTextView;
    private RecyclerView recyclerView;
    private SectionedRecyclerViewAdapter sectionAdapter;
    private Context applicationContext;
    private Handler handler;

    public HomeScreenService(HashSet<String> tickerSet, ProgressBar progressBar, TextView loadingTextView,
                             RecyclerView recyclerView, SectionedRecyclerViewAdapter sectionAdapter,
                             Context applicationContext, Handler handler) {
        this.tickerSet = tickerSet;
        this.progressBar = progressBar;
        this.loadingTextView = loadingTextView;
        this.recyclerView = recyclerView;
        this.sectionAdapter = sectionAdapter;
        this.applicationContext = applicationContext;
        this.handler = handler;
    }

    @Override
    public void run() {
        StockAppClient.getInstance(applicationContext).fetchHomeScreenData(tickerSet,
                progressBar, loadingTextView, recyclerView, sectionAdapter);
        handler.postDelayed(this, 5000);
    }
}

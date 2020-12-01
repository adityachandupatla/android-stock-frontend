package com.csci571.aditya.stockapp.network;

import android.content.Context;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.csci571.aditya.stockapp.localstorage.AppStorage;
import com.csci571.aditya.stockapp.localstorage.FavoriteStorageModel;
import com.csci571.aditya.stockapp.localstorage.PortfolioStorageModel;

import java.util.ArrayList;
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

    public HomeScreenService(ProgressBar progressBar, TextView loadingTextView,
                             RecyclerView recyclerView, SectionedRecyclerViewAdapter sectionAdapter,
                             Context applicationContext, Handler handler) {
        this.tickerSet = uniqueTickers(AppStorage.getPortfolio(applicationContext), AppStorage.getFavorites(applicationContext));
        this.progressBar = progressBar;
        this.loadingTextView = loadingTextView;
        this.recyclerView = recyclerView;
        this.sectionAdapter = sectionAdapter;
        this.applicationContext = applicationContext;
        this.handler = handler;
    }

    @Override
    public void run() {
        StockAppClient.getInstance().fetchHomeScreenData(tickerSet,
                progressBar, loadingTextView, recyclerView, sectionAdapter, applicationContext);
        handler.postDelayed(this, 15000);
    }

    private HashSet<String> uniqueTickers(ArrayList<PortfolioStorageModel> portfolioStorageModels, ArrayList<FavoriteStorageModel> favoriteStorageModels) {
        HashSet<String> myset = new HashSet<>();
        for (PortfolioStorageModel portfolioStorageModel: portfolioStorageModels) {
            myset.add(portfolioStorageModel.getStockTicker());
        }
        for (FavoriteStorageModel favoriteStorageModel: favoriteStorageModels) {
            myset.add(favoriteStorageModel.getStockTicker());
        }
        return myset;
    }
}

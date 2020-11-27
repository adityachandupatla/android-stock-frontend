package com.csci571.aditya.stockapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.csci571.aditya.stockapp.favorite.Favorite;
import com.csci571.aditya.stockapp.favorite.FavoriteSection;
import com.csci571.aditya.stockapp.info.SectionInfoFactory;
import com.csci571.aditya.stockapp.info.SectionItemInfoDialog;
import com.csci571.aditya.stockapp.info.SectionItemInfoFactory;
import com.csci571.aditya.stockapp.localstorage.AppStorage;
import com.csci571.aditya.stockapp.localstorage.FavoriteStorageModel;
import com.csci571.aditya.stockapp.localstorage.PortfolioStorageModel;
import com.csci571.aditya.stockapp.portfolio.Portfolio;
import com.csci571.aditya.stockapp.portfolio.PortfolioSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;



public class MainActivity extends AppCompatActivity implements PortfolioSection.ClickListener, FavoriteSection.ClickListener {

    private SectionedRecyclerViewAdapter sectionAdapter;

    private static final String TAG = "com.csci571.aditya.stockapp.MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_StockApplication);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ArrayList<PortfolioStorageModel> portfolioStorageModels = AppStorage.getPortfolio(getApplicationContext());
        ArrayList<FavoriteStorageModel> favoriteStorageModels = AppStorage.getFavorites(getApplicationContext());

        Log.i(TAG, "Portfolio details:");
        for (PortfolioStorageModel portfolioStorageModel: portfolioStorageModels) {
            Log.i(TAG, portfolioStorageModel.toString());
        }

        Log.i(TAG, "Favorite details:");
        for (FavoriteStorageModel favoriteStorageModel: favoriteStorageModels) {
            Log.i(TAG, favoriteStorageModel.toString());
        }

        HashSet<String> tickerSet = uniqueTickers(portfolioStorageModels, favoriteStorageModels);
        for (String ticker: tickerSet) {
            Log.i(TAG, "ticker: " + ticker);
        }

        sectionAdapter = new SectionedRecyclerViewAdapter();

        List<Portfolio> portfolioList = new ArrayList<>();
        portfolioList.add(new Portfolio("MSFT", 8.0, 202.68, 10.57));
        portfolioList.add(new Portfolio("MSFT", 8.0, 202.68, 10.57));

        List<Favorite> favList = new ArrayList<>();
        favList.add(new Favorite("MSFT", 8.0, "Microsoft Corp.", 202.68, 10.57));
        favList.add(new Favorite("MSFT", 8.0, "Microsoft Corp.", 202.68, 10.57));

        sectionAdapter.addSection(new PortfolioSection("19450.26", portfolioList, this));
        sectionAdapter.addSection(new FavoriteSection(favList, this));

        // Set up your RecyclerView with the SectionedRecyclerViewAdapter
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(sectionAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
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

    @Override
    public void onItemRootViewClicked(@NonNull PortfolioSection section, int itemAdapterPosition) {
        final SectionItemInfoDialog dialog = SectionItemInfoDialog.getInstance(
                SectionItemInfoFactory.create(itemAdapterPosition, sectionAdapter),
                SectionInfoFactory.create(section, sectionAdapter.getAdapterForSection(section))
        );
        dialog.show(getSupportFragmentManager(), "ABCD");
    }

    @Override
    public void onItemRootViewClicked(@NonNull FavoriteSection section, int itemAdapterPosition) {
        final SectionItemInfoDialog dialog = SectionItemInfoDialog.getInstance(
                SectionItemInfoFactory.create(itemAdapterPosition, sectionAdapter),
                SectionInfoFactory.create(section, sectionAdapter.getAdapterForSection(section))
        );
        dialog.show(getSupportFragmentManager(), "EFGH");
    }
}
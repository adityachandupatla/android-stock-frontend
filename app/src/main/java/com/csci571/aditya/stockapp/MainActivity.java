package com.csci571.aditya.stockapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.csci571.aditya.stockapp.favorite.Favorite;
import com.csci571.aditya.stockapp.favorite.FavoriteSection;
import com.csci571.aditya.stockapp.info.SectionInfoFactory;
import com.csci571.aditya.stockapp.info.SectionItemInfoDialog;
import com.csci571.aditya.stockapp.info.SectionItemInfoFactory;
import com.csci571.aditya.stockapp.localstorage.AppStorage;
import com.csci571.aditya.stockapp.localstorage.FavoriteStorageModel;
import com.csci571.aditya.stockapp.localstorage.PortfolioStorageModel;
import com.csci571.aditya.stockapp.network.StockAppClient;
import com.csci571.aditya.stockapp.portfolio.Portfolio;
import com.csci571.aditya.stockapp.portfolio.PortfolioSection;
import com.csci571.aditya.stockapp.utils.Constants;
import com.csci571.aditya.stockapp.utils.Parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


public class MainActivity extends AppCompatActivity implements PortfolioSection.ClickListener, FavoriteSection.ClickListener {

    private RecyclerView recyclerView;
    private SectionedRecyclerViewAdapter sectionAdapter;

    private static final String TAG = "com.csci571.aditya.stockapp.MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_StockApplication);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        double uninvestedCash = AppStorage.getUninvestedCash(getApplicationContext());
        ArrayList<PortfolioStorageModel> portfolioStorageModels = AppStorage.getPortfolio(getApplicationContext());
        ArrayList<FavoriteStorageModel> favoriteStorageModels = AppStorage.getFavorites(getApplicationContext());
        HashSet<String> tickerSet = uniqueTickers(portfolioStorageModels, favoriteStorageModels);

        sectionAdapter = new SectionedRecyclerViewAdapter();

        List<Portfolio> portfolioList = new ArrayList<>();
        for (PortfolioStorageModel portfolioStorageModel: portfolioStorageModels) {
            portfolioList.add(new Portfolio(portfolioStorageModel.getStockTicker(),
                    portfolioStorageModel.getSharesOwned(), 0, 0,
                    portfolioStorageModel.getTotalAmount()));
        }
        sectionAdapter.addSection(Constants.PORTFOLIO_SECTION_TAG,
                new PortfolioSection(Parser.beautify(uninvestedCash), portfolioList, this));

        List<Favorite> favList = new ArrayList<>();
        for (FavoriteStorageModel favoriteStorageModel: favoriteStorageModels) {
            double shares = getSharesOfFavoriteStock(favoriteStorageModel.getStockTicker(), portfolioStorageModels);
            favList.add(new Favorite(favoriteStorageModel.getStockTicker(), shares, favoriteStorageModel.getCompanyName(),
                    0, 0, favoriteStorageModel.getLastPrice()));
        }
        sectionAdapter.addSection(Constants.FAVORITE_SECTION_TAG,
                new FavoriteSection(favList, this));

        recyclerView = findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(sectionAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        enableSwipeToDelete();

        ProgressBar progressBar = findViewById(R.id.progressBar);
        TextView loadingTextView = findViewById(R.id.loading_text);

        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        loadingTextView.setVisibility(View.VISIBLE);

        StockAppClient.getInstance(getApplicationContext()).fetchHomeScreenData(tickerSet,
                progressBar, loadingTextView, recyclerView, sectionAdapter);

    }

    private double getSharesOfFavoriteStock(String stockTicker, ArrayList<PortfolioStorageModel> portfolioStorageModels) {
        for (PortfolioStorageModel portfolioStorageModel: portfolioStorageModels) {
            if (portfolioStorageModel.getStockTicker().equals(stockTicker)) {
                return portfolioStorageModel.getSharesOwned();
            }
        }
        return 0;
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

    private void enableSwipeToDelete() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                String ticker = ((TextView) viewHolder.itemView.findViewById(R.id.ticker_fav)).getText().toString();
                FavoriteSection favoriteSection = (FavoriteSection) sectionAdapter.getSection(Constants.FAVORITE_SECTION_TAG);
                List<Favorite> favoriteList = favoriteSection.getList();
                for (Favorite favorite: favoriteList) {
                    if (favorite.getTicker().equals(ticker)) {
                        favoriteList.remove(favorite);
                        break;
                    }
                }
                AppStorage.removeFromFavorite(getApplicationContext(), ticker);
                sectionAdapter.notifyItemRemoved(position);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder targetHolder) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = targetHolder.getAdapterPosition();

                PortfolioSection portfolioSection = (PortfolioSection) sectionAdapter.getSection(Constants.PORTFOLIO_SECTION_TAG);
                List<Portfolio> portfolioList = portfolioSection.getList();

                FavoriteSection favoriteSection = (FavoriteSection) sectionAdapter.getSection(Constants.FAVORITE_SECTION_TAG);
                List<Favorite> favoriteList = favoriteSection.getList();

                if (viewHolder.itemView.findViewById(R.id.ticker) != null &&
                        targetHolder.itemView.findViewById(R.id.ticker) != null) {
                    Log.i(TAG, "from: " + fromPosition + " to: " + toPosition + " size: " + portfolioList.size());
                    if (fromPosition < toPosition) {
                        for (int i = fromPosition - 1; i < toPosition - 1; i++) {
                            Collections.swap(portfolioList, i, i + 1);
                        }
                    } else {
                        for (int i = fromPosition - 1; i > toPosition - 1; i--) {
                            Collections.swap(portfolioList, i, i - 1);
                        }
                    }
                    AppStorage.updatePortfolioOrder(getApplicationContext(), portfolioList);
                    sectionAdapter.notifyItemMoved(fromPosition, toPosition);
                    Log.i(TAG, "New Size: " + portfolioList.size());
                    return super.onMove(recyclerView, viewHolder, targetHolder);
                }
                if (viewHolder.itemView.findViewById(R.id.ticker_fav) != null &&
                        targetHolder.itemView.findViewById(R.id.ticker_fav) != null) {
                    // ensure the offsets are correctly established before accessing the list
                    // this is done because favorite list is below portfolio list
                    int offset = portfolioList.size() + 1;
                    fromPosition -= offset;
                    toPosition -= offset;
                    Log.i(TAG, "from: " + fromPosition + " to: " + toPosition + " size: " + favoriteList.size());
                    if (fromPosition < toPosition) {
                        for (int i = fromPosition - 1; i < toPosition - 1; i++) {
                            Collections.swap(favoriteList, i, i + 1);
                        }
                    } else {
                        for (int i = fromPosition - 1; i > toPosition - 1; i--) {
                            Collections.swap(favoriteList, i, i - 1);
                        }
                    }
                    AppStorage.updateFavoriteOrder(getApplicationContext(), favoriteList);
                    // re-add the offsets
                    sectionAdapter.notifyItemMoved(fromPosition + offset, toPosition + offset);
                    Log.i(TAG, "New Size: " + favoriteList.size());
                    return super.onMove(recyclerView, viewHolder, targetHolder);
                }
                return false;
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }
}
package com.csci571.aditya.stockapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.csci571.aditya.stockapp.favorite.Favorite;
import com.csci571.aditya.stockapp.favorite.FavoriteSection;
import com.csci571.aditya.stockapp.localstorage.AppStorage;
import com.csci571.aditya.stockapp.localstorage.FavoriteStorageModel;
import com.csci571.aditya.stockapp.localstorage.PortfolioStorageModel;
import com.csci571.aditya.stockapp.network.HomeScreenService;
import com.csci571.aditya.stockapp.portfolio.Portfolio;
import com.csci571.aditya.stockapp.portfolio.PortfolioSection;
import com.csci571.aditya.stockapp.search.SearchMain;
import com.csci571.aditya.stockapp.swipedrag.SwipeDragCallback;
import com.csci571.aditya.stockapp.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SectionedRecyclerViewAdapter sectionAdapter;
    private HomeScreenService homeScreenService;
    private Handler handler;

    private static final String TAG = "com.csci571.aditya.stockapp.MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_StockApplication);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.inflateMenu(R.menu.search_menu);
        myToolbar.setTitle(R.string.app_name);

        SearchMain.implementSearch(myToolbar, (SearchManager) getSystemService(Context.SEARCH_SERVICE), getComponentName(),
                this, getApplicationContext(), (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));

        sectionAdapter = new SectionedRecyclerViewAdapter();

        sectionAdapter.addSection(Constants.PORTFOLIO_SECTION_TAG,
                new PortfolioSection(MainActivity.this, getApplicationContext()));

        sectionAdapter.addSection(Constants.FAVORITE_SECTION_TAG,
                new FavoriteSection(MainActivity.this, getApplicationContext()));

        recyclerView = findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(sectionAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        enableSwipeDrag();

        ProgressBar progressBar = findViewById(R.id.progressBar);
        TextView loadingTextView = findViewById(R.id.loading_text);

        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        loadingTextView.setVisibility(View.VISIBLE);

        handler = new Handler();
        homeScreenService = new HomeScreenService(progressBar, loadingTextView, recyclerView, sectionAdapter,
                getApplicationContext(), handler);
    }

    @Override
    protected void onStart() {
        super.onStart();

        PortfolioSection portfolioSection = (PortfolioSection) sectionAdapter.getSection(Constants.PORTFOLIO_SECTION_TAG);
        ArrayList<PortfolioStorageModel> portfolioStorageModels = AppStorage.getPortfolio(getApplicationContext());
        List<Portfolio> portfolioList = new ArrayList<>();
        for (PortfolioStorageModel portfolioStorageModel: portfolioStorageModels) {
            portfolioList.add(new Portfolio(portfolioStorageModel.getStockTicker(),
                    portfolioStorageModel.getSharesOwned(), portfolioStorageModel.getTotalAmount(),
                    portfolioStorageModel.getStockPrice()));
        }
        portfolioSection.setList(portfolioList);

        FavoriteSection favoriteSection = (FavoriteSection) sectionAdapter.getSection(Constants.FAVORITE_SECTION_TAG);
        ArrayList<FavoriteStorageModel> favoriteStorageModels = AppStorage.getFavorites(getApplicationContext());
        List<Favorite> favoriteList = new ArrayList<>();
        for (FavoriteStorageModel favoriteStorageModel: favoriteStorageModels) {
            double shares = favoriteSection.getSharesOfFavoriteStock(favoriteStorageModel.getStockTicker(),
                    portfolioStorageModels);
            favoriteList.add(new Favorite(favoriteStorageModel.getStockTicker(), shares, favoriteStorageModel.getCompanyName(),
                    favoriteStorageModel.getLastPrice(), favoriteStorageModel.getStockPrice()));
        }
        favoriteSection.setList(favoriteList);

        sectionAdapter.notifyDataSetChanged();

        handler.post(homeScreenService);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(homeScreenService);
    }

    private void enableSwipeDrag() {
        SwipeDragCallback swipeDragCallback = new SwipeDragCallback(this) {
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
                    return super.onMove(recyclerView, viewHolder, targetHolder);
                }
                if (viewHolder.itemView.findViewById(R.id.ticker_fav) != null &&
                        targetHolder.itemView.findViewById(R.id.ticker_fav) != null) {
                    // ensure the offsets are correctly established before accessing the list
                    // this is done because favorite list is below portfolio list
                    int offset = portfolioList.size() + 1;
                    fromPosition -= offset;
                    toPosition -= offset;
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
                    return super.onMove(recyclerView, viewHolder, targetHolder);
                }
                return false;
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeDragCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }
}
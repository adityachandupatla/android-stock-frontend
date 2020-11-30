package com.csci571.aditya.stockapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.csci571.aditya.stockapp.localstorage.AppStorage;
import com.csci571.aditya.stockapp.localstorage.FavoriteStorageModel;
import com.csci571.aditya.stockapp.models.DetailScreenWrapperModel;
import com.csci571.aditya.stockapp.network.StockAppClient;
import com.csci571.aditya.stockapp.utils.Constants;
import com.google.android.material.appbar.AppBarLayout;

public class DetailActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    private final String TAG = "com.csci571.aditya.stockapp.DetailActivity";
    private String ticker;
    private boolean isFavorite;
    private DetailScreenWrapperModel data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        ticker = intent.getStringExtra(Constants.INTENT_TICKER_EXTRA);
        isFavorite = AppStorage.isFavorite(getApplicationContext(), ticker);

        // setup toolbar
        Toolbar myToolbar = findViewById(R.id.detail_toolbar);
        myToolbar.setTitle(R.string.app_name);
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        myToolbar.setNavigationOnClickListener(v -> {
            NavUtils.navigateUpFromSameTask(DetailActivity.this);
        });

        // setup menu and its functionality in the toolbar
        myToolbar.inflateMenu(R.menu.detail_menu);
        MenuItem menuItem = myToolbar.getMenu().findItem(R.id.action_favorite);
        if (isFavorite) {
            menuItem.setIcon(R.drawable.ic_baseline_star_24);
        }
        myToolbar.setOnMenuItemClickListener(this);

        ProgressBar progressBar = findViewById(R.id.detailProgressBar);
        TextView loadingTextView = findViewById(R.id.detailLoadingText);
        NestedScrollView nestedScrollView = findViewById(R.id.nested_scroll_view);

        nestedScrollView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        loadingTextView.setVisibility(View.VISIBLE);

        data = new DetailScreenWrapperModel();
        StockAppClient.getInstance(getApplicationContext()).fetchDetailScreenData(ticker,
                progressBar, loadingTextView, nestedScrollView, data);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_favorite) {
            if (isFavorite) {
                AppStorage.removeFromFavorite(getApplicationContext(), ticker);
                Toast.makeText(this, "\"" + ticker + "\" was removed from favorites",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                String companyName = data.getOutlookModel().getCompanyName();
                double lastPrice = data.getSummaryModel().getLastPrice();
                FavoriteStorageModel favoriteStorageModel = new FavoriteStorageModel(ticker, companyName,
                        lastPrice);
                AppStorage.addToFavorite(getApplicationContext(), favoriteStorageModel);
                Toast.makeText(this, "\"" + ticker + "\" was added to favorites",
                        Toast.LENGTH_SHORT).show();
            }
            isFavorite = !isFavorite;
            return true;
        }
        return false;
    }
}
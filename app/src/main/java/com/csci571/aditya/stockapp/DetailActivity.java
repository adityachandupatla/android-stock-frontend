package com.csci571.aditya.stockapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.csci571.aditya.stockapp.network.StockAppClient;
import com.csci571.aditya.stockapp.utils.Constants;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String ticker = intent.getStringExtra(Constants.INTENT_TICKER_EXTRA);

        Toolbar myToolbar = findViewById(R.id.detail_toolbar);
        myToolbar.setTitle(R.string.app_name);

        ProgressBar progressBar = findViewById(R.id.detailProgressBar);
        TextView loadingTextView = findViewById(R.id.detailLoadingText);
        NestedScrollView nestedScrollView = findViewById(R.id.nested_scroll_view);

        nestedScrollView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        loadingTextView.setVisibility(View.VISIBLE);

        StockAppClient.getInstance(getApplicationContext()).fetchDetailScreenData(ticker,
                progressBar, loadingTextView, nestedScrollView);
    }
}
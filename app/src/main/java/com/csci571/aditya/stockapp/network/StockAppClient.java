package com.csci571.aditya.stockapp.network;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.csci571.aditya.stockapp.favorite.Favorite;
import com.csci571.aditya.stockapp.favorite.FavoriteSection;
import com.csci571.aditya.stockapp.localstorage.AppStorage;
import com.csci571.aditya.stockapp.models.AutoSuggestModel;
import com.csci571.aditya.stockapp.models.Suggestion;
import com.csci571.aditya.stockapp.models.SummaryModel;
import com.csci571.aditya.stockapp.portfolio.Portfolio;
import com.csci571.aditya.stockapp.portfolio.PortfolioSection;
import com.csci571.aditya.stockapp.search.AutoSuggestAdapter;
import com.csci571.aditya.stockapp.utils.Constants;
import com.csci571.aditya.stockapp.utils.Parser;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import androidx.recyclerview.widget.RecyclerView;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class StockAppClient {

    private static StockAppClient mInstance;
    private static final String TAG = "com.csci571.aditya.stockapp.network.StockAppClient";
    private String host;
    private Context mCtx;

    private StockAppClient(Context context) {
        host = Constants.DEVELOPMENT_HOST;
        mCtx = context;
//        host = Constants.PROD_HOST;
    }

    public static synchronized StockAppClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new StockAppClient(context);
        }
        return mInstance;
    }

    private void fillHomeScreen(ProgressBar progressBar, TextView loadingTextView, RecyclerView recyclerView,
                                SectionedRecyclerViewAdapter sectionAdapter, Map<String, Double> map) {

        PortfolioSection portfolioSection = (PortfolioSection) sectionAdapter.getSection(Constants.PORTFOLIO_SECTION_TAG);
        List<Portfolio> portfolioList = portfolioSection.getList();
        double stockWorth = 0;
        for (Portfolio portfolio: portfolioList) {
            if (map.containsKey(portfolio.getTicker())) {
                double currentStockPrice = map.get(portfolio.getTicker());
                double averagePriceOfStockOwned = portfolio.getTotalAmountOwned() / portfolio.getShares();
                double changePercentage = currentStockPrice - averagePriceOfStockOwned;
                portfolio.setStockPrice(currentStockPrice);
                portfolio.setChangePercentage(changePercentage);
                portfolio.updateChangeImage();
                stockWorth += currentStockPrice * portfolio.getShares();
            }
        }
        double uninvestedCash = AppStorage.getUninvestedCash(mCtx);
        portfolioSection.setNetWorth(Parser.beautify(uninvestedCash + stockWorth));

        FavoriteSection favoriteSection = (FavoriteSection) sectionAdapter.getSection(Constants.FAVORITE_SECTION_TAG);
        List<Favorite> favoriteList = favoriteSection.getList();
        for (Favorite favorite: favoriteList) {
            if (map.containsKey(favorite.getTicker())) {
                double currentStockPrice = map.get(favorite.getTicker());
                double changePercentage = currentStockPrice - favorite.getLastPrice();
                favorite.setStockPrice(currentStockPrice);
                favorite.setChangePercentage(changePercentage);
                favorite.updateChangeImage();
            }
        }

        sectionAdapter.notifyDataSetChanged();

        progressBar.setVisibility(View.INVISIBLE);
        loadingTextView.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void fetchHomeScreenData(HashSet<String> tickerSet, ProgressBar progressBar,
                                    TextView loadingTextView, RecyclerView recyclerView, SectionedRecyclerViewAdapter sectionAdapter) {
        Log.i(TAG, "<<<<<<<<<<<<  FETCHING HOME SCREEN DATA >>>>>>>>>>>>>");
        Map<String, Double> map = new HashMap<>();
        final AtomicInteger requests = new AtomicInteger(tickerSet.size());
        for (String ticker: tickerSet) {
            String url = host + String.format(Constants.SUMMARY_ENDPOINT_TEMPLATE, ticker);
            makeRequest(url, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject result) throws JSONException {
                    SummaryModel summaryModel = new Gson().fromJson(result.toString(), SummaryModel.class);
                    map.put(ticker, summaryModel.getLastPrice());
                    int status = requests.decrementAndGet();
                    if (status == 0) {
                        fillHomeScreen(progressBar, loadingTextView, recyclerView, sectionAdapter, map);
                    }
                }

                @Override
                public void onError(String result) throws Exception {
                    Log.e(TAG, "Error occurred while making request to backend: ");
                    Log.e(TAG, result);
                    int status = requests.decrementAndGet();
                    if (status == 0) {
                        fillHomeScreen(progressBar, loadingTextView, recyclerView, sectionAdapter, map);
                    }
                }
            });
        }
    }

    public void fetchAutoSuggestData(String searchString, AutoSuggestAdapter autoSuggestAdapter) {
        String url = host + String.format(Constants.AUTOCOMPLETE_ENDPOINT_TEMPLATE, searchString);
        makeRequest(url, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                AutoSuggestModel autoSuggestModel = new Gson().fromJson(result.toString(), AutoSuggestModel.class);
                if (autoSuggestModel.isSuccess() && autoSuggestModel.getData().size() > 0) {
                    List<String> data = new ArrayList<>();
                    for (Suggestion suggestion: autoSuggestModel.getData()) {
                        data.add(suggestion.getTicker() + " - " + suggestion.getName());
                    }
                    autoSuggestAdapter.setData(data);
                    autoSuggestAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String result) throws Exception {
                Log.e(TAG, "Error occurred while making request to backend: ");
                Log.e(TAG, result);
            }
        });
    }

    // Custom JSON Request Handler
    public void makeRequest(final String url, final VolleyCallback callback) {
        //Pass response to success callback
        CustomJSONObjectRequest rq = new CustomJSONObjectRequest(Request.Method.GET,
                url, null, result -> {
            try {
                callback.onSuccess(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            try {
                callback.onError(error.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Request added to the RequestQueue
        VolleyController.getInstance(mCtx).addToRequestQueue(rq);
    }

}

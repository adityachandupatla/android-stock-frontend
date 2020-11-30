package com.csci571.aditya.stockapp.network;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.csci571.aditya.stockapp.R;
import com.csci571.aditya.stockapp.favorite.Favorite;
import com.csci571.aditya.stockapp.favorite.FavoriteSection;
import com.csci571.aditya.stockapp.localstorage.AppStorage;
import com.csci571.aditya.stockapp.models.AutoSuggestModel;
import com.csci571.aditya.stockapp.models.DetailScreenWrapperModel;
import com.csci571.aditya.stockapp.models.NewsModel;
import com.csci571.aditya.stockapp.models.OutlookModel;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
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

    // Custom JSON Request Handler
    private void makeRequest(final String url, final VolleyCallback callback) {
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

    private void fillDetailScreen(ProgressBar progressBar, TextView loadingTextView,
                                  NestedScrollView nestedScrollView, DetailScreenWrapperModel data) {

        OutlookModel outlookModel = data.getOutlookModel();
        SummaryModel summaryModel = data.getSummaryModel();
        NewsModel newsModel = data.getNewsModel();

        String ticker = outlookModel.getStockTickerSymbol();
        double currentStockPrice = summaryModel.getLastPrice();

        TextView tickerTextView = nestedScrollView.findViewById(R.id.tickerTextView);
        tickerTextView.setText(ticker);

        TextView companyNameTextView = nestedScrollView.findViewById(R.id.companyNameTextView);
        companyNameTextView.setText(outlookModel.getCompanyName());

        TextView stockPriceTextView = nestedScrollView.findViewById(R.id.stockPriceTextView);
        String stockPriceDisplayText = "$" + Parser.beautify(currentStockPrice);
        stockPriceTextView.setText(stockPriceDisplayText);

        TextView changeTextView = nestedScrollView.findViewById(R.id.changeTextView);
        String changeDisplayText = "";
        double change = summaryModel.getLastPrice() - summaryModel.getPreviousClosingPrice();
        if (change > 0) {
            changeDisplayText = "$" + Parser.beautify(change);
            changeTextView.setTextColor(ContextCompat.getColor(mCtx, R.color.positiveChange));
        }
        else if (change < 0) {
            changeDisplayText = "-$" + Parser.beautify(-1 * change);
            changeTextView.setTextColor(ContextCompat.getColor(mCtx, R.color.negativeChange));
        }
        else {
            changeDisplayText = "$0";
            changeTextView.setTextColor(ContextCompat.getColor(mCtx, R.color.noChange));
        }
        changeTextView.setText(changeDisplayText);

        TextView sharesOwnedTextView = nestedScrollView.findViewById(R.id.sharesOwnedTextView);
        TextView marketValueTextView = nestedScrollView.findViewById(R.id.marketValueTextView);
        double sharesOwned = AppStorage.getSharesOwned(mCtx, ticker);
        String sharesOwnedDisplayText;
        String marketValueDisplayText;
        if (sharesOwned > 0) {
            sharesOwnedDisplayText = "Shares Owned: " + Parser.beautify(sharesOwned);
            marketValueDisplayText = "Market Value: $" + Parser.beautify(sharesOwned * currentStockPrice);

        }
        else {
            sharesOwnedDisplayText = "You have 0 shares of " + ticker + ".";
            marketValueDisplayText = "Start Trading!";
        }
        sharesOwnedTextView.setText(sharesOwnedDisplayText);
        marketValueTextView.setText(marketValueDisplayText);

        TextView currentPriceTextView = nestedScrollView.findViewById(R.id.currentPriceTextView);
        String currentPriceDisplayText = "Current Price: " + Parser.beautify(summaryModel.getLastPrice());
        currentPriceTextView.setText(currentPriceDisplayText);

        TextView lowPriceTextView = nestedScrollView.findViewById(R.id.lowPriceTextView);
        String lowPriceDisplayText = "Low: " + Parser.beautify(summaryModel.getLowPrice());
        lowPriceTextView.setText(lowPriceDisplayText);

        TextView bidPriceTextView = nestedScrollView.findViewById(R.id.bidPriceTextView);
        String bidPriceDisplayText = "Bid Price: " + Parser.beautify(summaryModel.getBidPrice());
        bidPriceTextView.setText(bidPriceDisplayText);

        TextView openPriceTextView = nestedScrollView.findViewById(R.id.openPriceTextView);
        String openPriceDisplaytext = "Open Price: " + Parser.beautify(summaryModel.getOpeningPrice());
        openPriceTextView.setText(openPriceDisplaytext);

        TextView midPriceTextView = nestedScrollView.findViewById(R.id.midPriceTextView);
        String midPriceDisplayText = "Mid: " + Parser.beautify(summaryModel.getMidPrice());
        midPriceTextView.setText(midPriceDisplayText);

        TextView highPriceTextView = nestedScrollView.findViewById(R.id.highPriceTextView);
        String highPriceDisplayText = "High: " + Parser.beautify(summaryModel.getHighPrice());
        highPriceTextView.setText(highPriceDisplayText);

        TextView volumeTextView = nestedScrollView.findViewById(R.id.volumeTextView);
        String volumeDisplayText = "Volume: " + Parser.beautify(summaryModel.getVolume());
        volumeTextView.setText(volumeDisplayText);

        TextView aboutContentTextView = nestedScrollView.findViewById(R.id.aboutContentTextView);
        aboutContentTextView.setText(outlookModel.getDescription());

        progressBar.setVisibility(View.INVISIBLE);
        loadingTextView.setVisibility(View.INVISIBLE);
        nestedScrollView.setVisibility(View.VISIBLE);
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

    public void fetchDetailScreenData(String ticker, ProgressBar progressBar, TextView loadingTextView,
                                      NestedScrollView nestedScrollView) {
        String outlookUrl = host + String.format(Constants.OUTLOOK_ENDPOINT_TEMPLATE, ticker);
        String summaryUrl = host + String.format(Constants.SUMMARY_ENDPOINT_TEMPLATE, ticker);
        String newsUrl = host + String.format(Constants.NEWS_ENDPOINT_TEMPLATE, ticker);

        DetailScreenWrapperModel data = new DetailScreenWrapperModel();

        final AtomicInteger requests = new AtomicInteger(Constants.DETAIL_SCREEN_REQUESTS);

        makeRequest(summaryUrl, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                SummaryModel summaryModel = new Gson().fromJson(result.toString(), SummaryModel.class);
                data.setSummaryModel(summaryModel);
                int status = requests.decrementAndGet();
                if (status == 0) {
                    fillDetailScreen(progressBar, loadingTextView, nestedScrollView, data);
                }
            }

            @Override
            public void onError(String result) throws Exception {
                Log.e(TAG, "Error occurred while making request to backend: ");
                Log.e(TAG, result);
                int status = requests.decrementAndGet();
                if (status == 0) {
                    fillDetailScreen(progressBar, loadingTextView, nestedScrollView, data);
                }
            }
        });

        makeRequest(outlookUrl, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                OutlookModel outlookModel = new Gson().fromJson(result.toString(), OutlookModel.class);
                data.setOutlookModel(outlookModel);
                int status = requests.decrementAndGet();
                if (status == 0) {
                    fillDetailScreen(progressBar, loadingTextView, nestedScrollView, data);
                }
            }

            @Override
            public void onError(String result) throws Exception {
                Log.e(TAG, "Error occurred while making request to backend: ");
                Log.e(TAG, result);
                int status = requests.decrementAndGet();
                if (status == 0) {
                    fillDetailScreen(progressBar, loadingTextView, nestedScrollView, data);
                }
            }
        });

        makeRequest(newsUrl, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                NewsModel newsModel = new Gson().fromJson(result.toString(), NewsModel.class);
                data.setNewsModel(newsModel);
                int status = requests.decrementAndGet();
                if (status == 0) {
                    fillDetailScreen(progressBar, loadingTextView, nestedScrollView, data);
                }
            }

            @Override
            public void onError(String result) throws Exception {
                Log.e(TAG, "Error occurred while making request to backend: ");
                Log.e(TAG, result);
                int status = requests.decrementAndGet();
                if (status == 0) {
                    fillDetailScreen(progressBar, loadingTextView, nestedScrollView, data);
                }
            }
        });
    }
}

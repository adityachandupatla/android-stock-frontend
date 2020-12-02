package com.csci571.aditya.stockapp.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.csci571.aditya.stockapp.R;
import com.csci571.aditya.stockapp.favorite.Favorite;
import com.csci571.aditya.stockapp.favorite.FavoriteSection;
import com.csci571.aditya.stockapp.localstorage.AppStorage;
import com.csci571.aditya.stockapp.models.ArticleModel;
import com.csci571.aditya.stockapp.models.AutoSuggestModel;
import com.csci571.aditya.stockapp.models.DetailScreenWrapperModel;
import com.csci571.aditya.stockapp.models.NewsModel;
import com.csci571.aditya.stockapp.models.OutlookModel;
import com.csci571.aditya.stockapp.models.Suggestion;
import com.csci571.aditya.stockapp.models.SummaryModel;
import com.csci571.aditya.stockapp.news.NewsAdapter;
import com.csci571.aditya.stockapp.portfolio.Portfolio;
import com.csci571.aditya.stockapp.portfolio.PortfolioSection;
import com.csci571.aditya.stockapp.search.AutoSuggestAdapter;
import com.csci571.aditya.stockapp.utils.Constants;
import com.csci571.aditya.stockapp.utils.Parser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

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
    private final String host;

    private StockAppClient() {
        host = Constants.HOST;
    }

    public static synchronized StockAppClient getInstance() {
        if (mInstance == null) {
            mInstance = new StockAppClient();
        }
        return mInstance;
    }

    // Custom JSON Request Handler
    private void makeRequest(final String url, final VolleyCallback callback, Context context) {
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

        rq.setRetryPolicy(new DefaultRetryPolicy(50000, 5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Request added to the RequestQueue
        VolleyController.getInstance(context).addToRequestQueue(rq);
    }

    private void fillHomeScreen(ProgressBar progressBar, TextView loadingTextView, RecyclerView recyclerView,
                                SectionedRecyclerViewAdapter sectionAdapter, Map<String, Double> map, Context context) {

        PortfolioSection portfolioSection = (PortfolioSection) sectionAdapter.getSection(Constants.PORTFOLIO_SECTION_TAG);
        List<Portfolio> portfolioList = portfolioSection.getList();
        double stockWorth = 0;
        for (Portfolio portfolio: portfolioList) {
            double currentStockPrice = 0;
            if (map.containsKey(portfolio.getTicker())) {
                currentStockPrice = map.get(portfolio.getTicker());
                AppStorage.setPortfolioStockPrice(context, portfolio.getTicker(), currentStockPrice);
            }
            else {
                Log.e(TAG, "Unable to get currentStockPrice for the ticker: "
                        + portfolio.getTicker() + " using currentStockPrice as 0");
            }
            double averagePriceOfStockOwned = portfolio.getTotalAmountOwned() / portfolio.getShares();
            double changePercentage = currentStockPrice - averagePriceOfStockOwned;
            portfolio.setStockPrice(currentStockPrice);
            portfolio.setChangePercentage(changePercentage);
            stockWorth += currentStockPrice * portfolio.getShares();
        }
        double uninvestedCash = AppStorage.getUninvestedCash(context);
        portfolioSection.setNetWorth(Parser.beautify(uninvestedCash + stockWorth));

        FavoriteSection favoriteSection = (FavoriteSection) sectionAdapter.getSection(Constants.FAVORITE_SECTION_TAG);
        List<Favorite> favoriteList = favoriteSection.getList();
        for (Favorite favorite: favoriteList) {
            double currentStockPrice = 0;
            if (map.containsKey(favorite.getTicker())) {
                currentStockPrice = map.get(favorite.getTicker());
                AppStorage.setFavoriteStockPrice(context, favorite.getTicker(), currentStockPrice);
            }
            else {
                Log.e(TAG, "Unable to get currentStockPrice for the ticker: "
                        + favorite.getTicker() + " using currentStockPrice as 0");
            }
            double changePercentage = currentStockPrice - favorite.getLastPrice();
            favorite.setStockPrice(currentStockPrice);
            favorite.setChangePercentage(changePercentage);
        }

        sectionAdapter.notifyDataSetChanged();

        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.INVISIBLE);
            loadingTextView.setVisibility(View.INVISIBLE);
        }
        if (recyclerView.getVisibility() == View.INVISIBLE) {
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void fillDetailScreen(ProgressBar progressBar, TextView loadingTextView,
                                  NestedScrollView nestedScrollView, NewsAdapter newsAdapter,
                                  DetailScreenWrapperModel data, Context context) {

        OutlookModel outlookModel = data.getOutlookModel();
        if (outlookModel == null) {
            outlookModel = new OutlookModel();
            outlookModel.setCompanyName("No company");
            outlookModel.setStockTickerSymbol("");
            outlookModel.setDescription("No description");
        }
        SummaryModel summaryModel = data.getSummaryModel();
        if (summaryModel == null) {
            summaryModel = new SummaryModel();
        }
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
            changeTextView.setTextColor(ContextCompat.getColor(context, R.color.positiveChange));
        }
        else if (change < 0) {
            changeDisplayText = "-$" + Parser.beautify(-1 * change);
            changeTextView.setTextColor(ContextCompat.getColor(context, R.color.negativeChange));
        }
        else {
            changeDisplayText = "$0";
            changeTextView.setTextColor(ContextCompat.getColor(context, R.color.noChange));
        }
        changeTextView.setText(changeDisplayText);

        TextView sharesOwnedTextView = nestedScrollView.findViewById(R.id.sharesOwnedTextView);
        TextView marketValueTextView = nestedScrollView.findViewById(R.id.marketValueTextView);
        double sharesOwned = ticker.length() > 0 ? AppStorage.getSharesOwned(context, ticker) : 0;
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
        TextView toggleAboutDescTextView = nestedScrollView.findViewById(R.id.toggleAboutDescTextView);
        toggleAboutDescTextView.setOnClickListener(v -> {
            if (v.getVisibility() == View.VISIBLE) {
                TextView textView = ((TextView) v);
                String message = textView.getText().toString();
                if (message.equals(Constants.SHOW_MORE_MESSAGE)) {
                    aboutContentTextView.setMaxLines(Integer.MAX_VALUE);
                    textView.setText(Constants.SHOW_LESS_MESSAGE);
                }
                else {
                    aboutContentTextView.setMaxLines(2);
                    textView.setText(Constants.SHOW_MORE_MESSAGE);
                }
            }
        });
        ViewTreeObserver vto = aboutContentTextView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(() -> {
            Layout layout = aboutContentTextView.getLayout();
            if(layout != null) {
                int lines = layout.getLineCount();
                if(lines > 0) {
                    int ellipsisCount = layout.getEllipsisCount(lines - 1);
                    if (ellipsisCount > 0) {
                        toggleAboutDescTextView.setText(Constants.SHOW_MORE_MESSAGE);
                    }
                    else {
                        if (lines > 2) {
                            toggleAboutDescTextView.setText(Constants.SHOW_LESS_MESSAGE);
                        }
                        else {
                            toggleAboutDescTextView.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        });

        if (ticker.length() > 0) {
            WebView webview = nestedScrollView.findViewById(R.id.highcharts_webview);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.clearCache(true);
            webview.getSettings().setDomStorageEnabled(true);
            String historicalUrl = host + String.format(Constants.HISTORICAL_ENDPOINT_TEMPLATE, ticker);
            String jsUrl = String.format(Constants.JS_FUNCTION_BUILDER_TEMPLATE, historicalUrl, ticker);
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if (url.equals(Constants.HIGHCHARTS_ASSET_PATH)) {
                        webview.loadUrl(jsUrl);
                    }
                }
            });
            webview.loadUrl(Constants.HIGHCHARTS_ASSET_PATH);
        }

        if (newsModel != null && newsModel.getArticles() != null && newsModel.getArticles().size() > 0) {
            newsAdapter.setArticles(newsModel.getArticles());
            newsAdapter.notifyDataSetChanged();
        }

        progressBar.setVisibility(View.INVISIBLE);
        loadingTextView.setVisibility(View.INVISIBLE);
        nestedScrollView.setVisibility(View.VISIBLE);
    }

    public void fetchHomeScreenData(HashSet<String> tickerSet, ProgressBar progressBar,
                                    TextView loadingTextView, RecyclerView recyclerView,
                                    SectionedRecyclerViewAdapter sectionAdapter, Context context) {
        Map<String, Double> map = new HashMap<>();
        if (tickerSet.size() > 0) {
            Log.i(TAG, "<<<<<<<<<<<<  FETCHING HOME SCREEN DATA >>>>>>>>>>>>>");
            final AtomicInteger requests = new AtomicInteger(tickerSet.size());
            for (String ticker: tickerSet) {
                String url = host + String.format(Constants.SUMMARY_ENDPOINT_TEMPLATE, ticker);
                makeRequest(url, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        SummaryModel summaryModel;
                        try {
                            summaryModel = new Gson().fromJson(result.toString(), SummaryModel.class);
                            map.put(ticker, summaryModel.getLastPrice());
                        } catch(JsonSyntaxException e) {
                            Log.e(TAG, "Request: " + url + " returned: " + result.toString() +
                                    " which is not parsable into SummaryModel");
                        } finally {
                            int status = requests.decrementAndGet();
                            if (status == 0) {
                                fillHomeScreen(progressBar, loadingTextView, recyclerView, sectionAdapter, map, context);
                            }
                        }
                    }

                    @Override
                    public void onError(String result) {
                        Log.e(TAG, "Error occurred while making request: " + url + " to backend: ");
                        Log.e(TAG, result);
                        int status = requests.decrementAndGet();
                        if (status == 0) {
                            fillHomeScreen(progressBar, loadingTextView, recyclerView, sectionAdapter, map, context);
                        }
                    }
                }, context);
            }
        }
        else {
            fillHomeScreen(progressBar, loadingTextView, recyclerView, sectionAdapter, map, context);
        }
    }

    public void fetchAutoSuggestData(String searchString, AutoSuggestAdapter autoSuggestAdapter, Context context) {
        String url = host + String.format(Constants.AUTOCOMPLETE_ENDPOINT_TEMPLATE, searchString);
        makeRequest(url, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                AutoSuggestModel autoSuggestModel;
                try {
                    autoSuggestModel = new Gson().fromJson(result.toString(), AutoSuggestModel.class);
                    if (autoSuggestModel.isSuccess() && autoSuggestModel.getData().size() > 0) {
                        List<String> data = new ArrayList<>();
                        for (Suggestion suggestion: autoSuggestModel.getData()) {
                            data.add(suggestion.getTicker() + " - " + suggestion.getName());
                        }
                        autoSuggestAdapter.setData(data);
                        autoSuggestAdapter.notifyDataSetChanged();
                    }
                } catch (JsonSyntaxException e) {
                    Log.e(TAG, "Request: " + url + " returned: " + result.toString() +
                            " which is not parsable into AutoSuggestModel");
                }
            }

            @Override
            public void onError(String result) {
                Log.e(TAG, "Error occurred while making request: " + url + " to backend: ");
                Log.e(TAG, result);
            }
        }, context);
    }

    public void fetchDetailScreenData(String ticker, ProgressBar progressBar, TextView loadingTextView,
                                      NestedScrollView nestedScrollView, NewsAdapter newsAdapter,
                                      DetailScreenWrapperModel data, Context context) {
        String outlookUrl = host + String.format(Constants.OUTLOOK_ENDPOINT_TEMPLATE, ticker);
        String summaryUrl = host + String.format(Constants.SUMMARY_ENDPOINT_TEMPLATE, ticker);
        String newsUrl = host + String.format(Constants.NEWS_ENDPOINT_TEMPLATE, ticker);

        final AtomicInteger requests = new AtomicInteger(Constants.DETAIL_SCREEN_REQUESTS);

        makeRequest(summaryUrl, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                SummaryModel summaryModel;
                try {
                    summaryModel = new Gson().fromJson(result.toString(), SummaryModel.class);
                    data.setSummaryModel(summaryModel);
                } catch(JsonSyntaxException e) {
                    Log.e(TAG, "Request: " + summaryUrl + " returned: " + result.toString() +
                            " which is not parsable into SummaryModel");
                } finally {
                    int status = requests.decrementAndGet();
                    if (status == 0) {
                        fillDetailScreen(progressBar, loadingTextView, nestedScrollView, newsAdapter, data, context);
                    }
                }
            }

            @Override
            public void onError(String result) {
                Log.e(TAG, "Error occurred while making request: " + summaryUrl + " to backend: ");
                Log.e(TAG, result);
                int status = requests.decrementAndGet();
                if (status == 0) {
                    fillDetailScreen(progressBar, loadingTextView, nestedScrollView, newsAdapter, data, context);
                }
            }
        }, context);

        makeRequest(outlookUrl, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                OutlookModel outlookModel;
                try {
                    outlookModel = new Gson().fromJson(result.toString(), OutlookModel.class);
                    data.setOutlookModel(outlookModel);
                } catch (JsonSyntaxException e) {
                    Log.e(TAG, "Request: " + outlookUrl + " returned: " + result.toString() +
                            " which is not parsable into OutlookModel");
                } finally {
                    int status = requests.decrementAndGet();
                    if (status == 0) {
                        fillDetailScreen(progressBar, loadingTextView, nestedScrollView, newsAdapter, data, context);
                    }
                }
            }

            @Override
            public void onError(String result) {
                Log.e(TAG, "Error occurred while making request: " + outlookUrl + " to backend: ");
                Log.e(TAG, result);
                int status = requests.decrementAndGet();
                if (status == 0) {
                    fillDetailScreen(progressBar, loadingTextView, nestedScrollView, newsAdapter, data, context);
                }
            }
        }, context);

        makeRequest(newsUrl, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                NewsModel newsModel;
                try {
                    newsModel = new Gson().fromJson(result.toString(), NewsModel.class);
                    data.setNewsModel(newsModel);
                } catch (JsonSyntaxException e) {
                    Log.e(TAG, "Request: " + newsUrl + " returned: " + result.toString() +
                            " which is not parsable into NewsModel");
                } finally {
                    int status = requests.decrementAndGet();
                    if (status == 0) {
                        fillDetailScreen(progressBar, loadingTextView, nestedScrollView, newsAdapter, data, context);
                    }
                }
            }

            @Override
            public void onError(String result) throws Exception {
                Log.e(TAG, "Error occurred while making request: " + newsUrl + " to backend: ");
                Log.e(TAG, result);
                int status = requests.decrementAndGet();
                if (status == 0) {
                    fillDetailScreen(progressBar, loadingTextView, nestedScrollView, newsAdapter, data, context);
                }
            }
        }, context);
    }
}

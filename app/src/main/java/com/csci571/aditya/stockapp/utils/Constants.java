package com.csci571.aditya.stockapp.utils;

import com.csci571.aditya.stockapp.localstorage.FavoriteStorageModel;
import com.csci571.aditya.stockapp.localstorage.PortfolioStorageModel;

public class Constants {
    // Constants related to shared preferences
    public static final String PORTFOLIO_PREF_NAME = "portfolio";
    public static final String PORTFOLIO_KEY = "MyPortfolio";
    public static final String FAVORITES_PREF_NAME = "favorites";
    public static final String FAVORITES_KEY = "MyFavorites";
    public static final String APP_FIRST_TIME = "firstTime";
    public static final String UNINVESTED_PREF_NAME = "uninvested";
    public static final String UNINVESTED_KEY = "MyUninvestedCash";

    // Constants related to network calls
    public static final String DEVELOPMENT_HOST = "http://10.0.2.2:8080";
    public static final String PROD_HOST = "https://android-node-backend.wl.r.appspot.com";
    public static final String HOST = DEVELOPMENT_HOST;
    public static final String OUTLOOK_ENDPOINT_TEMPLATE = "/stock/api/v1.0/outlook/%s";
    public static final String SUMMARY_ENDPOINT_TEMPLATE = "/stock/api/v1.0/summary/%s";
    public static final String HISTORICAL_ENDPOINT_TEMPLATE = "/stock/api/v1.0/historical/%s";
    public static final String DAILY_ENDPOINT_TEMPLATE = "/stock/api/v1.0/daily/%s?startDate=%s&resampleFreq=4min";
    public static final String AUTOCOMPLETE_ENDPOINT_TEMPLATE = "/stock/api/v1.0/search?query=%s";
    public static final String NEWS_ENDPOINT_TEMPLATE = "/stock/api/v1.0/news/%s";
    public static final int DETAIL_SCREEN_REQUESTS = 3;

    // Default portfolio
    public static final String PORTFOLIO_SECTION_TAG = "PortfolioTag";
    public static PortfolioStorageModel MICROSOFT_PORTFOLIO = new PortfolioStorageModel("MSFT", 8.0, 1619.76);
    public static PortfolioStorageModel GOOGLE_PORTFOLIO = new PortfolioStorageModel("GOOGL", 5.0, 8080.55);
    public static PortfolioStorageModel APPLE_PORTFOLIO = new PortfolioStorageModel("AAPL", 5.0, 544.3);
    public static PortfolioStorageModel TESLA_PORTFOLIO = new PortfolioStorageModel("TSLA", 3.0, 1164.12);

    // Default favorites
    public static final String FAVORITE_SECTION_TAG = "FavoriteTag";
    public static FavoriteStorageModel NETFLIX_FAVORITE = new FavoriteStorageModel("NFLX", "NetFlix Inc", 491.36);
    public static FavoriteStorageModel GOOGLE_FAVORITE = new FavoriteStorageModel("GOOGL", "Alphabet Inc - Class A", 1787.02);
    public static FavoriteStorageModel APPLE_FAVORITE = new FavoriteStorageModel("AAPL", "Apple Inc", 116.59);
    public static FavoriteStorageModel TESLA_FAVORITE = new FavoriteStorageModel("TSLA", "Tesla Inc", 585.76);

    // Misc
    public static final String INTENT_TICKER_EXTRA = "IntentTicker";
    public static final String HIGHCHARTS_ASSET_PATH = "file:///android_asset/highcharts.html";
    public static final String JS_FUNCTION_BUILDER_TEMPLATE = "javascript:plotChart(\"%s\", \"%s\");";
    public static final String SHOW_MORE_MESSAGE = "Show more...";
    public static final String SHOW_LESS_MESSAGE = "Show less";
    public static final String TWITTER_URL = "https://twitter.com/intent/tweet?text=%s";
}

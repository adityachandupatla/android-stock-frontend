package com.csci571.aditya.stockapp.localstorage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.csci571.aditya.stockapp.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class AppStorage {

    private static final String TAG = "com.csci571.aditya.stockapp.localstorage.AppStorage";

    private static void fillPortfolioDefaults(SharedPreferences pref) {
        SharedPreferences.Editor editor = pref.edit();
        ArrayList<PortfolioStorageModel> defaultPortfolio = new ArrayList<>();
        defaultPortfolio.add(new PortfolioStorageModel("MSFT", 8.0, 1619.76));
        defaultPortfolio.add(new PortfolioStorageModel("GOOGL", 5.0, 8080.55));
        defaultPortfolio.add(new PortfolioStorageModel("AAPL", 5.0, 544.3));
        defaultPortfolio.add(new PortfolioStorageModel("TSLA", 3.0, 1164.12));

        Gson gson = new Gson();
        String json = gson.toJson(defaultPortfolio);
        editor.putString(Constants.PORTFOLIO_KEY, json);
        boolean commitResult = editor.commit();
        if (!commitResult) {
            Log.e(TAG, "Unable to commit the default portfolio into sharedPreferences local storage");
        }
    }

    private static void fillFavoritesDefaults(SharedPreferences pref) {
        SharedPreferences.Editor editor = pref.edit();
        ArrayList<FavoriteStorageModel> defaultFavorites = new ArrayList<>();
        defaultFavorites.add(new FavoriteStorageModel("NFLX", "NetFlix Inc"));
        defaultFavorites.add(new FavoriteStorageModel("GOOGL", "Alphabet Inc - Class A"));
        defaultFavorites.add(new FavoriteStorageModel("AAPL", "Apple Inc"));
        defaultFavorites.add(new FavoriteStorageModel("TSLA", "Tesla Inc"));

        Gson gson = new Gson();
        String json = gson.toJson(defaultFavorites);
        editor.putString(Constants.FAVORITES_KEY, json);
        boolean commitResult = editor.commit();
        if (!commitResult) {
            Log.e(TAG, "Unable to commit the default favorites into sharedPreferences local storage");
        }
    }

    public static ArrayList<PortfolioStorageModel>  getPortfolio(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Constants.PORTFOLIO_PREF_NAME, Context.MODE_PRIVATE);

        if (!pref.getBoolean(Constants.APP_FIRST_TIME, false)) {
            fillPortfolioDefaults(pref);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(Constants.APP_FIRST_TIME, true);
            boolean commitResult = editor.commit();
            if (!commitResult) {
                Log.e(TAG, "Unable to commit the " + Constants.APP_FIRST_TIME + " flag into sharedPreferences local storage");
            }
        }

        Gson gson = new Gson();
        ArrayList<PortfolioStorageModel> portfolioStorageModels = null;
        String json = pref.getString(Constants.PORTFOLIO_KEY, "");
        if (!json.equals("")) {
            portfolioStorageModels = gson.fromJson(json, new TypeToken<ArrayList<PortfolioStorageModel>>(){}.getType());
        }
        else {
            // empty array list
            portfolioStorageModels = new ArrayList<>();
        }
        return portfolioStorageModels;
    }

    public static ArrayList<FavoriteStorageModel>  getFavorites(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Constants.FAVORITES_PREF_NAME, Context.MODE_PRIVATE);

        if (!pref.getBoolean(Constants.APP_FIRST_TIME, false)) {
            fillFavoritesDefaults(pref);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(Constants.APP_FIRST_TIME, true);
            boolean commitResult = editor.commit();
            if (!commitResult) {
                Log.e(TAG, "Unable to commit the " + Constants.APP_FIRST_TIME + " flag into sharedPreferences local storage");
            }
        }

        Gson gson = new Gson();
        ArrayList<FavoriteStorageModel> favoriteStorageModels = null;
        String json = pref.getString(Constants.FAVORITES_KEY, "");
        if (!json.equals("")) {
            favoriteStorageModels = gson.fromJson(json, new TypeToken<ArrayList<FavoriteStorageModel>>(){}.getType());
        }
        else {
            // empty array list
            favoriteStorageModels = new ArrayList<>();
        }
        return favoriteStorageModels;
    }
}

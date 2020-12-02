package com.csci571.aditya.stockapp.localstorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.csci571.aditya.stockapp.favorite.Favorite;
import com.csci571.aditya.stockapp.portfolio.Portfolio;
import com.csci571.aditya.stockapp.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class AppStorage {

    private static final String TAG = "com.csci571.aditya.stockapp.localstorage.AppStorage";

    private static void fillPortfolioDefaults(SharedPreferences pref) {
        SharedPreferences.Editor editor = pref.edit();
        ArrayList<PortfolioStorageModel> defaultPortfolio = new ArrayList<>();
        defaultPortfolio.add(Constants.MICROSOFT_PORTFOLIO);
        defaultPortfolio.add(Constants.GOOGLE_PORTFOLIO);
        defaultPortfolio.add(Constants.APPLE_PORTFOLIO);
        defaultPortfolio.add(Constants.TESLA_PORTFOLIO);

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
        defaultFavorites.add(Constants.NETFLIX_FAVORITE);
        defaultFavorites.add(Constants.GOOGLE_FAVORITE);
        defaultFavorites.add(Constants.APPLE_FAVORITE);
        defaultFavorites.add(Constants.TESLA_FAVORITE);

        Gson gson = new Gson();
        String json = gson.toJson(defaultFavorites);
        editor.putString(Constants.FAVORITES_KEY, json);
        boolean commitResult = editor.commit();
        if (!commitResult) {
            Log.e(TAG, "Unable to commit the default favorites into sharedPreferences local storage");
        }
    }

    private static void fillNetWorthDefaults(SharedPreferences pref) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(Constants.UNINVESTED_KEY, Constants.DEFAULT_UNINVESTED_AMOUNT);
        boolean commitResult = editor.commit();
        if (!commitResult) {
            Log.e(TAG, "Unable to commit the default net worth into sharedPreferences local storage");
        }
    }

    private static void setUninvestedCash(Context context, double uninvestedCash) {
        SharedPreferences pref = context.getSharedPreferences(Constants.UNINVESTED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(Constants.UNINVESTED_KEY, (float) uninvestedCash);
        boolean commitResult = editor.commit();
        if (!commitResult) {
            Log.e(TAG, "Unable to commit uninvested cash into sharedPreferences local storage");
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

    public static double getUninvestedCash(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Constants.UNINVESTED_PREF_NAME, Context.MODE_PRIVATE);

        if (!pref.getBoolean(Constants.APP_FIRST_TIME, false)) {
            fillNetWorthDefaults(pref);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(Constants.APP_FIRST_TIME, true);
            boolean commitResult = editor.commit();
            if (!commitResult) {
                Log.e(TAG, "Unable to commit the " + Constants.APP_FIRST_TIME + " flag into sharedPreferences local storage");
            }
        }

        return pref.getFloat(Constants.UNINVESTED_KEY, (float) 0.0);
    }

    public static void removeFromFavorite(Context context, String ticker) {
        SharedPreferences pref = context.getSharedPreferences(Constants.FAVORITES_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString(Constants.FAVORITES_KEY, "");
        if (!json.equals("")) {
            ArrayList<FavoriteStorageModel> favoriteStorageModels = gson.fromJson(json, new TypeToken<ArrayList<FavoriteStorageModel>>(){}.getType());
            for (FavoriteStorageModel favoriteStorageModel: favoriteStorageModels) {
                if (favoriteStorageModel.getStockTicker().equals(ticker)) {
                    favoriteStorageModels.remove(favoriteStorageModel);
                    break;
                }
            }
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(Constants.FAVORITES_KEY, gson.toJson(favoriteStorageModels));
            boolean commitResult = editor.commit();
            if (!commitResult) {
                Log.e(TAG, "Unable to commit the new favorites into sharedPreferences local storage");
            }
        }
    }

    public static void addToFavorite(Context context, FavoriteStorageModel newFavoriteStorageModel) {
        SharedPreferences pref = context.getSharedPreferences(Constants.FAVORITES_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString(Constants.FAVORITES_KEY, "");
        if (!json.equals("")) {
            ArrayList<FavoriteStorageModel> favoriteStorageModels = gson.fromJson(json, new TypeToken<ArrayList<FavoriteStorageModel>>(){}.getType());
            favoriteStorageModels.add(newFavoriteStorageModel);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(Constants.FAVORITES_KEY, gson.toJson(favoriteStorageModels));
            boolean commitResult = editor.commit();
            if (!commitResult) {
                Log.e(TAG, "Unable to commit the new favorites into sharedPreferences local storage");
            }
        }
    }

    public static void updatePortfolioOrder(Context context, List<Portfolio> portfolioList) {
        SharedPreferences pref = context.getSharedPreferences(Constants.PORTFOLIO_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString(Constants.PORTFOLIO_KEY, "");
        if (!json.equals("")) {
            ArrayList<PortfolioStorageModel> originalOrder = gson.fromJson(json, new TypeToken<ArrayList<PortfolioStorageModel>>(){}.getType());
            ArrayList<PortfolioStorageModel> newOrder = new ArrayList<>();
            for (Portfolio portfolio: portfolioList) {
                for (PortfolioStorageModel portfolioStorageModel: originalOrder) {
                    if (portfolioStorageModel.getStockTicker().equals(portfolio.getTicker())) {
                        newOrder.add(portfolioStorageModel);
                        break;
                    }
                }
            }
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(Constants.PORTFOLIO_KEY, gson.toJson(newOrder));
            boolean commitResult = editor.commit();
            if (!commitResult) {
                Log.e(TAG, "Unable to commit the new portfolio order into sharedPreferences local storage");
            }
        }
    }

    public static void updateFavoriteOrder(Context context, List<Favorite> favoriteList) {
        SharedPreferences pref = context.getSharedPreferences(Constants.FAVORITES_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString(Constants.FAVORITES_KEY, "");
        if (!json.equals("")) {
            ArrayList<FavoriteStorageModel> originalOrder = gson.fromJson(json, new TypeToken<ArrayList<FavoriteStorageModel>>(){}.getType());
            ArrayList<FavoriteStorageModel> newOrder = new ArrayList<>();
            for (Favorite favorite: favoriteList) {
                for (FavoriteStorageModel favoriteStorageModel: originalOrder) {
                    if (favoriteStorageModel.getStockTicker().equals(favorite.getTicker())) {
                        newOrder.add(favoriteStorageModel);
                        break;
                    }
                }
            }
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(Constants.FAVORITES_KEY, gson.toJson(newOrder));
            boolean commitResult = editor.commit();
            if (!commitResult) {
                Log.e(TAG, "Unable to commit the new favorites order into sharedPreferences local storage");
            }
        }
    }

    public static double getSharesOwned(Context context, String ticker) {
        SharedPreferences pref = context.getSharedPreferences(Constants.PORTFOLIO_PREF_NAME, Context.MODE_PRIVATE);
        double sharesOwned = 0;
        Gson gson = new Gson();
        ArrayList<PortfolioStorageModel> portfolioStorageModels = null;
        String json = pref.getString(Constants.PORTFOLIO_KEY, "");
        if (!json.equals("")) {
            portfolioStorageModels = gson.fromJson(json, new TypeToken<ArrayList<PortfolioStorageModel>>(){}.getType());
            for (PortfolioStorageModel portfolioStorageModel: portfolioStorageModels) {
                if (portfolioStorageModel.getStockTicker().equals(ticker)) {
                    sharesOwned = portfolioStorageModel.getSharesOwned();
                    break;
                }
            }
        }
        return sharesOwned;
    }

    public static boolean isFavorite(Context context, String ticker) {
        ArrayList<FavoriteStorageModel> favoriteStorageModels = getFavorites(context);
        for (FavoriteStorageModel favoriteStorageModel: favoriteStorageModels) {
            if (favoriteStorageModel.getStockTicker().equals(ticker)) {
                return true;
            }
        }
        return false;
    }

    public static void updatePortfolioStock(Context context, String ticker, double newShares,
                                            double newStockPrice, boolean buy) {
        SharedPreferences pref = context.getSharedPreferences(Constants.PORTFOLIO_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString(Constants.PORTFOLIO_KEY, "");
        if (!json.equals("")) {
            ArrayList<PortfolioStorageModel> portfolioStorageModels = gson.fromJson(json, new TypeToken<ArrayList<PortfolioStorageModel>>(){}.getType());
            double uninvestedCash = getUninvestedCash(context);
            double transactionValue = newShares * newStockPrice;
            boolean updated = false;
            Log.i(TAG, "========== Previous uninvested cash: " + uninvestedCash + " ==========");
            Log.i(TAG, "========== Transaction value: " + transactionValue +
                    " computed as: " + newShares + " * " + newStockPrice + " ==========");
            for (PortfolioStorageModel portfolioStorageModel: portfolioStorageModels) {
                if (portfolioStorageModel.getStockTicker().equals(ticker)) {
                    double prevSharesOwned = portfolioStorageModel.getSharesOwned();
                    double prevTotalAmount = portfolioStorageModel.getTotalAmount();
                    if (buy) {
                        Log.i(TAG, "========== Buying ==========");
                        setUninvestedCash(context, uninvestedCash - transactionValue);
                        double newTotalAmount = prevTotalAmount + (newShares * newStockPrice);
                        portfolioStorageModel.setSharesOwned(prevSharesOwned + newShares);
                        portfolioStorageModel.setTotalAmount(newTotalAmount);
                    }
                    else {
                        Log.i(TAG, "========== Selling ==========");
                        setUninvestedCash(context, uninvestedCash + transactionValue);
                        if (prevSharesOwned - newShares == 0) {
                            portfolioStorageModels.remove(portfolioStorageModel);
                        }
                        else {
                            double avgPricePerStock = prevTotalAmount / prevSharesOwned;
                            double newTotalAmount = prevTotalAmount - (newShares * avgPricePerStock);
                            portfolioStorageModel.setSharesOwned(prevSharesOwned - newShares);
                            portfolioStorageModel.setTotalAmount(newTotalAmount);
                        }
                    }
                    Log.i(TAG, "========== New uninvested cash: " + getUninvestedCash(context) + " ==========");
                    updated = true;
                    break;
                }
            }

            if (!updated && buy) {
                Log.i(TAG, "========== Buying ==========");
                setUninvestedCash(context, uninvestedCash - transactionValue);
                Log.i(TAG, "========== New uninvested cash: " + getUninvestedCash(context) + " ==========");
                PortfolioStorageModel portfolioStorageModel = new PortfolioStorageModel(ticker, newShares,
                        newShares * newStockPrice);
                portfolioStorageModels.add(portfolioStorageModel);
            }

            SharedPreferences.Editor editor = pref.edit();
            editor.putString(Constants.PORTFOLIO_KEY, gson.toJson(portfolioStorageModels));
            boolean commitResult = editor.commit();
            if (!commitResult) {
                Log.e(TAG, "Unable to commit the new portfolio after transaction into sharedPreferences local storage");
            }
        }
    }
}

package com.csci571.aditya.stockapp.search;

import android.util.Log;

import androidx.appcompat.widget.SearchView;

public class SearchOnQueryTextListener implements SearchView.OnQueryTextListener {

    private String TAG = "com.csci571.aditya.stockapp.search.SearchOnQueryTextListener";

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.i(TAG, "Search keyword is " + query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}

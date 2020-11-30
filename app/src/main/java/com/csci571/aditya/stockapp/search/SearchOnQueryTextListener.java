package com.csci571.aditya.stockapp.search;

import android.util.Log;

import androidx.appcompat.widget.SearchView;

public class SearchOnQueryTextListener implements SearchView.OnQueryTextListener {

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}

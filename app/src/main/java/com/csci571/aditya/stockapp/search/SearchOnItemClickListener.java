package com.csci571.aditya.stockapp.search;

import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.widget.SearchView;

public class SearchOnItemClickListener implements AdapterView.OnItemClickListener {

    private SearchView.SearchAutoComplete searchAutoComplete;

    public SearchOnItemClickListener(SearchView.SearchAutoComplete searchAutoComplete) {
        this.searchAutoComplete = searchAutoComplete;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
        String queryString = (String) adapterView.getItemAtPosition(itemIndex);
        searchAutoComplete.setText(queryString);
    }
}

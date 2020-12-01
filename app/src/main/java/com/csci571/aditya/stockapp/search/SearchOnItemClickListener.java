package com.csci571.aditya.stockapp.search;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.csci571.aditya.stockapp.DetailActivity;
import com.csci571.aditya.stockapp.utils.Constants;

import androidx.appcompat.widget.SearchView;

public class SearchOnItemClickListener implements AdapterView.OnItemClickListener {

    private SearchView.SearchAutoComplete searchAutoComplete;
    private Context context;

    public SearchOnItemClickListener(Context context, SearchView.SearchAutoComplete searchAutoComplete) {
        this.context = context;
        this.searchAutoComplete = searchAutoComplete;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
        String queryString = (String) adapterView.getItemAtPosition(itemIndex);
        String ticker = queryString.split("-")[0].trim();
        searchAutoComplete.setText(queryString);
        Intent myIntent = new Intent(context, DetailActivity.class);
        myIntent.putExtra(Constants.INTENT_TICKER_EXTRA, ticker);
        context.startActivity(myIntent);
    }
}

package com.csci571.aditya.stockapp.search;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.csci571.aditya.stockapp.network.StockAppClient;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;

public class SearchHandler implements Handler.Callback {
    private final int TRIGGER_AUTO_COMPLETE;
    private final SearchView.SearchAutoComplete searchAutoComplete;
    private final Context context;
    private final AutoSuggestAdapter autoSuggestAdapter;

    public SearchHandler(int TRIGGER_AUTO_COMPLETE, SearchView.SearchAutoComplete searchAutoComplete,
                         Context context, AutoSuggestAdapter autoSuggestAdapter) {
        this.TRIGGER_AUTO_COMPLETE = TRIGGER_AUTO_COMPLETE;
        this.searchAutoComplete = searchAutoComplete;
        this.context = context;
        this.autoSuggestAdapter = autoSuggestAdapter;
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        if (msg.what == TRIGGER_AUTO_COMPLETE) {
            if (!TextUtils.isEmpty(searchAutoComplete.getText())) {
                StockAppClient.getInstance(context)
                        .fetchAutoSuggestData(searchAutoComplete.getText().toString(),
                                autoSuggestAdapter);
            }
        }
        return false;
    }
}

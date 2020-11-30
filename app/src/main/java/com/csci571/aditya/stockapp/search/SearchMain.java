package com.csci571.aditya.stockapp.search;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.csci571.aditya.stockapp.R;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

public class SearchMain {

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private static Handler handler;

    public static void implementSearch(Toolbar myToolbar, SearchManager searchManager, ComponentName componentName,
                                       Context context, Context appContext, InputMethodManager inputMethodManager) {
        // Get the SearchView and set the searchable configuration
        Menu menu = myToolbar.getMenu();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        // Get SearchView autocomplete object.
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView
                .findViewById(R.id.search_src_text);

        AutoSuggestAdapter autoSuggestAdapter = new AutoSuggestAdapter(context,
                android.R.layout.simple_dropdown_item_1line);

        searchAutoComplete.setAdapter(autoSuggestAdapter);

        // Setup handler to get suggestions
        handler = new Handler(new SearchHandler(TRIGGER_AUTO_COMPLETE, searchAutoComplete,
                appContext, autoSuggestAdapter));
        // Listen to search view item on click event.
        searchAutoComplete.setOnItemClickListener(new SearchOnItemClickListener(context, searchAutoComplete));
        // Listen to text changes
        searchAutoComplete.addTextChangedListener(new SearchTextWatcher(TRIGGER_AUTO_COMPLETE, AUTO_COMPLETE_DELAY, handler));
        // Listen to submit events
        searchView.setOnQueryTextListener(new SearchOnQueryTextListener());

        searchMenuItem.setOnActionExpandListener(new SearchMenuExpandListener(searchView,
                inputMethodManager));
    }
}

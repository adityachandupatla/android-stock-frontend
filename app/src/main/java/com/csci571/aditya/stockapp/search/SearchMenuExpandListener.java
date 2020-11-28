package com.csci571.aditya.stockapp.search;

import android.os.Handler;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

public class SearchMenuExpandListener implements MenuItem.OnActionExpandListener {

    private SearchView searchView;
    private InputMethodManager imm;

    public SearchMenuExpandListener(SearchView searchView, InputMethodManager imm) {
        this.searchView = searchView;
        this.imm = imm;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item){
        // the search view is now open. add your logic if you want
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                searchView.requestFocus();
                if (imm != null) { // it's never null. I've added this line just to make the compiler happy
                    imm.showSoftInput(searchView.findFocus(), 0);
                }
            }
        });
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item){
        // the search view is closing. add your logic if you want
        return true;
    }
}

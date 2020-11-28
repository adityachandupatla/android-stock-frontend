package com.csci571.aditya.stockapp.favorite;

import android.util.Log;

public class FavoriteClickListener implements FavoriteSection.ClickListener {

    @Override
    public void onItemRootViewClicked(String ticker) {
        Log.i("MADDA", ticker);
    }
}

package com.csci571.aditya.stockapp.portfolio;

import android.util.Log;

public class PortfolioClickListener implements PortfolioSection.ClickListener {

    @Override
    public void onItemRootViewClicked(String ticker) {
        Log.i("MADDA", ticker);
    }
}

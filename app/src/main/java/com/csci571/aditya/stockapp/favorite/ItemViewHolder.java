package com.csci571.aditya.stockapp.favorite;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.csci571.aditya.stockapp.R;

import androidx.annotation.NonNull;

import static androidx.recyclerview.widget.RecyclerView.ViewHolder;

public class ItemViewHolder extends ViewHolder {
    private View rootView;

    private TextView tickerTextView;
    private TextView infoTextView;
    private TextView stockPriceTextView;
    private TextView changePercentageTextView;
    private ImageView changeImageView;
    private ImageView detailArrowImageView;

    ItemViewHolder(@NonNull View view) {
        super(view);

        rootView = view;

        tickerTextView = view.findViewById(R.id.ticker_fav);
        infoTextView = view.findViewById(R.id.shares_fav);
        stockPriceTextView = view.findViewById(R.id.stock_price_fav);
        changePercentageTextView = view.findViewById(R.id.change_percentage_fav);

        changeImageView = view.findViewById(R.id.change_fav);
        detailArrowImageView = view.findViewById(R.id.detail_arrow_fav);
    }

    public View getRootView() {
        return rootView;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    public TextView getTickerTextView() {
        return tickerTextView;
    }

    public void setTickerTextView(TextView tickerTextView) {
        this.tickerTextView = tickerTextView;
    }

    public TextView getInfoTextView() {
        return infoTextView;
    }

    public void setInfoTextView(TextView infoTextView) {
        this.infoTextView = infoTextView;
    }

    public TextView getStockPriceTextView() {
        return stockPriceTextView;
    }

    public void setStockPriceTextView(TextView stockPriceTextView) {
        this.stockPriceTextView = stockPriceTextView;
    }

    public TextView getChangePercentageTextView() {
        return changePercentageTextView;
    }

    public void setChangePercentageTextView(TextView changePercentageTextView) {
        this.changePercentageTextView = changePercentageTextView;
    }

    public ImageView getChangeImageView() {
        return changeImageView;
    }

    public void setChangeImageView(ImageView changeImageView) {
        this.changeImageView = changeImageView;
    }

    public ImageView getDetailArrowImageView() {
        return detailArrowImageView;
    }

    public void setDetailArrowImageView(ImageView detailArrowImageView) {
        this.detailArrowImageView = detailArrowImageView;
    }
}

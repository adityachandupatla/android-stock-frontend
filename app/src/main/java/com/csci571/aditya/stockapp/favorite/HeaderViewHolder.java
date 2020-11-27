package com.csci571.aditya.stockapp.favorite;

import android.view.View;
import android.widget.TextView;

import com.csci571.aditya.stockapp.R;

import androidx.recyclerview.widget.RecyclerView;

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    private TextView favoriteLabelTextView;

    public HeaderViewHolder(View view) {
        super(view);
        favoriteLabelTextView = view.findViewById(R.id.favorite_label_textview);
    }

    public TextView getFavoriteLabelTextView() {
        return favoriteLabelTextView;
    }

    public void setFavoriteLabelTextView(TextView favoriteLabelTextView) {
        this.favoriteLabelTextView = favoriteLabelTextView;
    }
}

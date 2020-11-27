package com.csci571.aditya.stockapp.portfolio;

import android.view.View;
import android.widget.TextView;

import com.csci571.aditya.stockapp.R;

import androidx.recyclerview.widget.RecyclerView;

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    private TextView portfolioLabelTextView;
    private TextView networthLabelTextView;
    private TextView networthValueTextView;

    public HeaderViewHolder(View view) {
        super(view);

        portfolioLabelTextView = view.findViewById(R.id.portfolio_label_textview);
        networthLabelTextView = view.findViewById(R.id.networth_label_textview);
        networthValueTextView = view.findViewById(R.id.networth_value_textview);
    }

    public TextView getPortfolioLabelTextView() {
        return portfolioLabelTextView;
    }

    public void setPortfolioLabelTextView(TextView portfolioLabelTextView) {
        this.portfolioLabelTextView = portfolioLabelTextView;
    }

    public TextView getNetworthLabelTextView() {
        return networthLabelTextView;
    }

    public void setNetworthLabelTextView(TextView networthLabelTextView) {
        this.networthLabelTextView = networthLabelTextView;
    }

    public TextView getNetworthValueTextView() {
        return networthValueTextView;
    }

    public void setNetworthValueTextView(TextView networthValueTextView) {
        this.networthValueTextView = networthValueTextView;
    }
}

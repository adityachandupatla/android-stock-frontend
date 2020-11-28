package com.csci571.aditya.stockapp.portfolio;

import android.graphics.Color;
import android.view.View;

import com.csci571.aditya.stockapp.R;
import com.csci571.aditya.stockapp.utils.Change;
import com.csci571.aditya.stockapp.utils.Parser;

import java.time.LocalDateTime;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

public class PortfolioSection extends Section {

    private String netWorth;
    private List<Portfolio> list;
    private ClickListener clickListener;

    public PortfolioSection(String netWorth, List<Portfolio> list, ClickListener clickListener) {

        super(SectionParameters.builder()
                .itemResourceId(R.layout.portfolio_item)
                .headerResourceId(R.layout.portfolio_header)
                .build());

        this.netWorth = netWorth;
        this.list = list;
        this.clickListener = clickListener;
    }

    public String getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(String netWorth) {
        this.netWorth = netWorth;
    }

    public List<Portfolio> getList() {
        return list;
    }

    public void setList(List<Portfolio> list) {
        this.list = list;
    }

    @Override
    public int getContentItemsTotal() {
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    private String buildSharesText(double shares) {
        return Parser.beautify(shares) + " shares";
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemHolder = (ItemViewHolder) holder;

        Portfolio portfolio = list.get(position);

        itemHolder.getTickerTextView().setText(portfolio.getTicker());
        itemHolder.getSharesTextView().setText(buildSharesText(portfolio.getShares()));
        itemHolder.getStockPriceTextView().setText(Parser.beautify(portfolio.getStockPrice()));
        itemHolder.getChangePercentageTextView().setText(Parser.beautify(portfolio.getChangePercentage()));

        itemHolder.getDetailArrowImageView().setImageResource(portfolio.getDetailArrowImage());
        if (portfolio.getChange() == Change.SAME) {
            itemHolder.getChangeImageView().setVisibility(View.INVISIBLE);
            itemHolder.getChangePercentageTextView().setTextColor(Color.GRAY);
        }
        else {
            itemHolder.getChangeImageView().setVisibility(View.VISIBLE);
            itemHolder.getChangeImageView().setImageResource(portfolio.getChangeImage());
            if (portfolio.getChange() == Change.INCREASE) {
                itemHolder.getChangePercentageTextView().setTextColor(Color.rgb(0, 179, 0));
            }
            else {
                itemHolder.getChangePercentageTextView().setTextColor(Color.rgb(255, 0, 38));
            }
        }

        itemHolder.getDetailArrowImageView().setOnClickListener(v ->
                clickListener.onItemRootViewClicked(this, itemHolder.getAdapterPosition())
        );
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(final View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(final RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
        headerHolder.getNetworthValueTextView().setText(netWorth);
        LocalDateTime now = LocalDateTime.now();
        String monthName = now.getMonth().toString();
        String formattedMonth = monthName.substring(0, 1).toUpperCase() + monthName.substring(1).toLowerCase();
        String dateText = formattedMonth + " " + now.getDayOfMonth() + ", " + now.getYear();
        headerHolder.getDateTextView().setText(dateText);
    }

    public interface ClickListener {
        void onItemRootViewClicked(@NonNull final PortfolioSection section, final int itemAdapterPosition);
    }
}

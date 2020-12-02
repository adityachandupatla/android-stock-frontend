package com.csci571.aditya.stockapp.portfolio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import com.csci571.aditya.stockapp.DetailActivity;
import com.csci571.aditya.stockapp.MainActivity;
import com.csci571.aditya.stockapp.R;
import com.csci571.aditya.stockapp.localstorage.AppStorage;
import com.csci571.aditya.stockapp.localstorage.PortfolioStorageModel;
import com.csci571.aditya.stockapp.utils.Change;
import com.csci571.aditya.stockapp.utils.Constants;
import com.csci571.aditya.stockapp.utils.Parser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

public class PortfolioSection extends Section {

    private String netWorth;
    private List<Portfolio> list;
    private final Context applicationContext;
    private final Context mainActivityContext;

    public PortfolioSection(Context mainActivityContext, Context applicationContext) {

        super(SectionParameters.builder()
                .itemResourceId(R.layout.portfolio_item)
                .headerResourceId(R.layout.portfolio_header)
                .build());

        this.mainActivityContext = mainActivityContext;
        this.applicationContext = applicationContext;

        // by default net worth will be equal to uninvested cash
        this.netWorth = Parser.beautify(AppStorage.getUninvestedCash(applicationContext));

        ArrayList<PortfolioStorageModel> portfolioStorageModels = AppStorage.getPortfolio(applicationContext);
        List<Portfolio> portfolioList = new ArrayList<>();
        for (PortfolioStorageModel portfolioStorageModel: portfolioStorageModels) {
            portfolioList.add(new Portfolio(portfolioStorageModel.getStockTicker(),
                    portfolioStorageModel.getSharesOwned(), portfolioStorageModel.getTotalAmount(),
                    portfolioStorageModel.getStockPrice()));
        }
        this.list = portfolioList;

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
        double changePercentage = portfolio.getChangePercentage();
        if (changePercentage < 0) {
            changePercentage = -1 * changePercentage;
        }
        itemHolder.getChangePercentageTextView().setText(Parser.beautify(changePercentage));

        itemHolder.getDetailArrowImageView().setImageResource(portfolio.getDetailArrowImage());
        if (portfolio.getChange() == Change.SAME) {
            itemHolder.getChangeImageView().setVisibility(View.INVISIBLE);
            itemHolder.getChangePercentageTextView().setTextColor(Color.GRAY);
        }
        else {
            itemHolder.getChangeImageView().setVisibility(View.VISIBLE);
            itemHolder.getChangeImageView().setImageResource(portfolio.getChangeImage());
            if (portfolio.getChange() == Change.INCREASE) {
                itemHolder.getChangePercentageTextView().setTextColor(ContextCompat.getColor(applicationContext, R.color.positiveChange));
            }
            else if (portfolio.getChange() == Change.DECREASE) {
                itemHolder.getChangePercentageTextView().setTextColor(ContextCompat.getColor(applicationContext, R.color.negativeChange));
            }
            else {
                itemHolder.getChangePercentageTextView().setTextColor(ContextCompat.getColor(applicationContext, R.color.noChange));
            }
        }

        itemHolder.itemView.setOnClickListener(v ->
                onItemRootViewClicked(portfolio.getTicker())
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

    public void onItemRootViewClicked(String ticker) {
        Intent myIntent = new Intent(mainActivityContext, DetailActivity.class);
        myIntent.putExtra(Constants.INTENT_TICKER_EXTRA, ticker);
        mainActivityContext.startActivity(myIntent);
    }
}

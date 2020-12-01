package com.csci571.aditya.stockapp.favorite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import com.csci571.aditya.stockapp.DetailActivity;
import com.csci571.aditya.stockapp.R;
import com.csci571.aditya.stockapp.localstorage.AppStorage;
import com.csci571.aditya.stockapp.localstorage.FavoriteStorageModel;
import com.csci571.aditya.stockapp.localstorage.PortfolioStorageModel;
import com.csci571.aditya.stockapp.utils.Change;
import com.csci571.aditya.stockapp.utils.Constants;
import com.csci571.aditya.stockapp.utils.Parser;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

public class FavoriteSection extends Section {
    private List<Favorite> list;
    private final Context applicationContext;
    private final Context mainActivityContext;

    public FavoriteSection(Context mainActivityContext, Context applicationContext) {

        super(SectionParameters.builder()
                .itemResourceId(R.layout.favorite_item)
                .headerResourceId(R.layout.favorite_header)
                .footerResourceId(R.layout.tiingo_footer)
                .build());

        this.mainActivityContext = mainActivityContext;
        this.applicationContext = applicationContext;

        ArrayList<FavoriteStorageModel> favoriteStorageModels = AppStorage.getFavorites(applicationContext);
        List<Favorite> favList = new ArrayList<>();
        for (FavoriteStorageModel favoriteStorageModel: favoriteStorageModels) {
            double shares = getSharesOfFavoriteStock(favoriteStorageModel.getStockTicker(),
                    AppStorage.getPortfolio(applicationContext));
            favList.add(new Favorite(favoriteStorageModel.getStockTicker(), shares, favoriteStorageModel.getCompanyName(),
                    favoriteStorageModel.getLastPrice()));
        }

        this.list = favList;
    }

    public List<Favorite> getList() {
        return list;
    }

    public void setList(List<Favorite> list) {
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

    private String buildSharesText(Favorite favorite) {
        if (favorite.getShares() == 0) {
            return favorite.getCompanyName();
        }
        else {
            return Parser.beautify(favorite.getShares()) + " shares";
        }
    }

    private double getSharesOfFavoriteStock(String stockTicker, ArrayList<PortfolioStorageModel> portfolioStorageModels) {
        for (PortfolioStorageModel portfolioStorageModel: portfolioStorageModels) {
            if (portfolioStorageModel.getStockTicker().equals(stockTicker)) {
                return portfolioStorageModel.getSharesOwned();
            }
        }
        return 0;
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemHolder = (ItemViewHolder) holder;

        Favorite favorite = list.get(position);

        itemHolder.getTickerTextView().setText(favorite.getTicker());

        // TODO: Determine whether we need to display share value or company name
        itemHolder.getInfoTextView().setText(buildSharesText(favorite));

        itemHolder.getStockPriceTextView().setText(Parser.beautify(favorite.getStockPrice()));
        double changePercentage = favorite.getChangePercentage();
        if (changePercentage < 0) {
            changePercentage = -1 * changePercentage;
        }
        itemHolder.getChangePercentageTextView().setText(Parser.beautify(changePercentage));

        itemHolder.getDetailArrowImageView().setImageResource(favorite.getDetailArrowImage());
        if (favorite.getChange() == Change.SAME) {
            itemHolder.getChangeImageView().setVisibility(View.INVISIBLE);
            itemHolder.getChangePercentageTextView().setTextColor(Color.GRAY);
        } else {
            itemHolder.getChangeImageView().setVisibility(View.VISIBLE);
            itemHolder.getChangeImageView().setImageResource(favorite.getChangeImage());
            if (favorite.getChange() == Change.INCREASE) {
                itemHolder.getChangePercentageTextView().setTextColor(ContextCompat.getColor(applicationContext,
                        R.color.positiveChange));
            }
            else if (favorite.getChange() == Change.DECREASE) {
                itemHolder.getChangePercentageTextView().setTextColor(ContextCompat.getColor(applicationContext,
                        R.color.negativeChange));
            }
            else {
                itemHolder.getChangePercentageTextView().setTextColor(ContextCompat.getColor(applicationContext,
                        R.color.noChange));
            }
        }

        itemHolder.getDetailArrowImageView().setOnClickListener(v ->
                onItemRootViewClicked(favorite.getTicker())
        );
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(final View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder getFooterViewHolder(final View view) {
        return new FooterViewHolder(view);
    }

    public void onItemRootViewClicked(String ticker) {
        Intent myIntent = new Intent(mainActivityContext, DetailActivity.class);
        myIntent.putExtra(Constants.INTENT_TICKER_EXTRA, ticker);
        ((Activity) mainActivityContext).startActivity(myIntent);
    }
}

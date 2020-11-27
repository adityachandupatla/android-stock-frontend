package com.csci571.aditya.stockapp.favorite;

import android.graphics.Color;
import android.view.View;

import com.csci571.aditya.stockapp.R;
import com.csci571.aditya.stockapp.utils.Change;
import com.csci571.aditya.stockapp.utils.Parser;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

public class FavoriteSection extends Section {
    private List<Favorite> list;
    private FavoriteSection.ClickListener clickListener;

    public FavoriteSection(List<Favorite> list, FavoriteSection.ClickListener clickListener) {

        super(SectionParameters.builder()
                .itemResourceId(R.layout.favorite_item)
                .headerResourceId(R.layout.favorite_header)
                .build());

        this.list = list;
        this.clickListener = clickListener;
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

        Favorite favorite = list.get(position);

        itemHolder.getTickerTextView().setText(favorite.getTicker());

        // TODO: Determine whether we need to display share value or company name
        itemHolder.getInfoTextView().setText(buildSharesText(favorite.getShares()));

        itemHolder.getStockPriceTextView().setText(Parser.beautify(favorite.getStockPrice()));
        itemHolder.getChangePercentageTextView().setText(Parser.beautify(favorite.getChangePercentage()));

        itemHolder.getDetailArrowImageView().setImageResource(favorite.getDetailArrowImage());
        if (favorite.getChange() == Change.SAME) {
            itemHolder.getChangeImageView().setVisibility(View.INVISIBLE);
            itemHolder.getChangePercentageTextView().setTextColor(Color.GRAY);
        }
        else {
            itemHolder.getChangeImageView().setVisibility(View.VISIBLE);
            itemHolder.getChangeImageView().setImageResource(favorite.getChangeImage());
            if (favorite.getChange() == Change.INCREASE) {
                itemHolder.getChangePercentageTextView().setTextColor(Color.rgb(0, 179, 0));
            }
            else {
                itemHolder.getChangePercentageTextView().setTextColor(Color.rgb(255, 0, 38));
            }
        }


        itemHolder.getRootView().setOnClickListener(v ->
                clickListener.onItemRootViewClicked(this, itemHolder.getAdapterPosition())
        );
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(final View view) {
        return new HeaderViewHolder(view);
    }

    public interface ClickListener {
        void onItemRootViewClicked(@NonNull final FavoriteSection section, final int itemAdapterPosition);
    }
}

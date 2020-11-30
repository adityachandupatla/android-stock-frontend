package com.csci571.aditya.stockapp.favorite;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.csci571.aditya.stockapp.R;
import com.csci571.aditya.stockapp.utils.Change;
import com.csci571.aditya.stockapp.utils.Parser;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

public class FavoriteSection extends Section {
    private List<Favorite> list;
    private FavoriteSection.ClickListener clickListener;
    private Context context;

    public FavoriteSection(List<Favorite> list, ClickListener clickListener, Context context) {

        super(SectionParameters.builder()
                .itemResourceId(R.layout.favorite_item)
                .headerResourceId(R.layout.favorite_header)
                .footerResourceId(R.layout.tiingo_footer)
                .build());
        this.list = list;
        this.clickListener = clickListener;
        this.context = context;
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

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemHolder = (ItemViewHolder) holder;

        Favorite favorite = list.get(position);

        itemHolder.getTickerTextView().setText(favorite.getTicker());

        // TODO: Determine whether we need to display share value or company name
        itemHolder.getInfoTextView().setText(buildSharesText(favorite));

        itemHolder.getStockPriceTextView().setText(Parser.beautify(favorite.getStockPrice()));
        itemHolder.getChangePercentageTextView().setText(Parser.beautify(favorite.getChangePercentage()));

        itemHolder.getDetailArrowImageView().setImageResource(favorite.getDetailArrowImage());
        if (favorite.getChange() == Change.SAME) {
            itemHolder.getChangeImageView().setVisibility(View.INVISIBLE);
            itemHolder.getChangePercentageTextView().setTextColor(Color.GRAY);
        } else {
            itemHolder.getChangeImageView().setVisibility(View.VISIBLE);
            itemHolder.getChangeImageView().setImageResource(favorite.getChangeImage());
            if (favorite.getChange() == Change.INCREASE) {
                itemHolder.getChangePercentageTextView().setTextColor(ContextCompat.getColor(context, R.color.positiveChange));
            }
            else if (favorite.getChange() == Change.DECREASE) {
                itemHolder.getChangePercentageTextView().setTextColor(ContextCompat.getColor(context, R.color.negativeChange));
            }
            else {
                itemHolder.getChangePercentageTextView().setTextColor(ContextCompat.getColor(context, R.color.noChange));
            }
        }

        itemHolder.getDetailArrowImageView().setOnClickListener(v ->
                clickListener.onItemRootViewClicked(this, favorite.getTicker())
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

    public interface ClickListener {
        void onItemRootViewClicked(FavoriteSection favoriteSection, String ticker);
    }
}

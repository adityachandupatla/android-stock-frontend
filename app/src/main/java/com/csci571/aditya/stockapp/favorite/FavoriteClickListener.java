package com.csci571.aditya.stockapp.favorite;

import com.csci571.aditya.stockapp.info.SectionInfoFactory;
import com.csci571.aditya.stockapp.info.SectionItemInfoDialog;
import com.csci571.aditya.stockapp.info.SectionItemInfoFactory;

import androidx.annotation.NonNull;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class FavoriteClickListener implements FavoriteSection.ClickListener {

    private SectionedRecyclerViewAdapter sectionAdapter;

    public FavoriteClickListener(SectionedRecyclerViewAdapter sectionAdapter) {
        this.sectionAdapter = sectionAdapter;
    }

    @Override
    public void onItemRootViewClicked(@NonNull FavoriteSection section, int itemAdapterPosition) {
//        final SectionItemInfoDialog dialog = SectionItemInfoDialog.getInstance(
//                SectionItemInfoFactory.create(itemAdapterPosition, sectionAdapter),
//                SectionInfoFactory.create(section, sectionAdapter.getAdapterForSection(section))
//        );
//        dialog.show(getSupportFragmentManager(), "EFGH");
    }
}

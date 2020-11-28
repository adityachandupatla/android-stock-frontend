package com.csci571.aditya.stockapp.portfolio;

import androidx.annotation.NonNull;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class PortfolioClickListener implements PortfolioSection.ClickListener {

    private SectionedRecyclerViewAdapter sectionAdapter;

    public PortfolioClickListener(SectionedRecyclerViewAdapter sectionAdapter) {
        this.sectionAdapter = sectionAdapter;
    }

    @Override
    public void onItemRootViewClicked(@NonNull PortfolioSection section, int itemAdapterPosition) {
//        final SectionItemInfoDialog dialog = SectionItemInfoDialog.getInstance(
//                SectionItemInfoFactory.create(itemAdapterPosition, sectionAdapter),
//                SectionInfoFactory.create(section, sectionAdapter.getAdapterForSection(section))
//        );
//        dialog.show(getSupportFragmentManager(), "ABCD");
    }
}

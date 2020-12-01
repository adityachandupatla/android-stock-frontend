package com.csci571.aditya.stockapp.news;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.csci571.aditya.stockapp.R;
import com.csci571.aditya.stockapp.models.ArticleModel;
import com.csci571.aditya.stockapp.utils.Parser;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = "com.csci571.aditya.stockapp.news.NewsAdapter";

    private List<ArticleModel> articles;

    public NewsAdapter() {
        articles = new ArrayList<>();
    }

    public List<ArticleModel> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleModel> articles) {
        this.articles = articles;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new TopNewsViewHolder(LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.topnews, parent, false));
        } else {
            return new RegularNewsViewHolder(LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.regularnews, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ArticleModel articleModel = articles.get(position);
        if (position == 0) {
            TopNewsViewHolder topNewsViewHolder = (TopNewsViewHolder) holder;

            ImageView imageView = topNewsViewHolder.getTopNewsImageView();
            Picasso.with(imageView.getContext()).load(articleModel.getImageUrl())
                    .resize(380,250).into(imageView);

            topNewsViewHolder.getTopNewsSourceTextView().setText(articleModel.getSource());
            topNewsViewHolder.getTopNewsDescriptionTextView().setText(articleModel.getTitle());

            String timeAgo = Parser.getTimeAgo(articleModel, position);
            topNewsViewHolder.getTopNewsTimeagoTextView().setText(timeAgo);

            topNewsViewHolder.setNewsUrl(articleModel.getArticleUrl());
            topNewsViewHolder.setImageUrl(articleModel.getImageUrl());
        }
        else {
            RegularNewsViewHolder regularNewsViewHolder = (RegularNewsViewHolder) holder;

            ImageView imageView = regularNewsViewHolder.getRegularNewsImageView();
            Picasso.with(imageView.getContext()).load(articleModel.getImageUrl())
                    .resize(150,150).into(imageView);

            regularNewsViewHolder.getRegularNewsSourceTextView().setText(articleModel.getSource());
            regularNewsViewHolder.getRegularNewsDescriptionTextView().setText(articleModel.getTitle());

            String timeAgo = Parser.getTimeAgo(articleModel, position);
            regularNewsViewHolder.getRegularNewsTimeagoTextView().setText(timeAgo);

            regularNewsViewHolder.setNewsUrl(articleModel.getArticleUrl());
            regularNewsViewHolder.setImageUrl(articleModel.getImageUrl());
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }
}

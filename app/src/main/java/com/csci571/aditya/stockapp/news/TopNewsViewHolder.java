package com.csci571.aditya.stockapp.news;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.csci571.aditya.stockapp.R;
import com.csci571.aditya.stockapp.utils.Constants;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import static com.csci571.aditya.stockapp.utils.Parser.urlEncode;

public class TopNewsViewHolder extends ViewHolder {
    private ImageView topNewsImageView;
    private TextView topNewsSourceTextView;
    private TextView topNewsTimeagoTextView;
    private TextView topNewsDescriptionTextView;
    private String newsUrl;
    private String imageUrl;

    public TopNewsViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(v -> {
            if (newsUrl != null && newsUrl.length() > 0 && URLUtil.isValidUrl(newsUrl)) {
                Uri uri = Uri.parse(newsUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                itemView.getContext().startActivity(intent);
            }
        });
        itemView.setOnLongClickListener(v -> {
            Context context = v.getContext();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View shareDialogLayout = ((Activity) context).getLayoutInflater().inflate(R.layout.share_dialog, null);
            builder.setView(shareDialogLayout);
            AlertDialog dialog = builder.create();

            ImageView shareDialogImageView = shareDialogLayout.findViewById(R.id.share_dialog_image);
            if (imageUrl != null && imageUrl.length() > 0 && URLUtil.isValidUrl(imageUrl)) {
                Picasso.with(shareDialogImageView.getContext()).load(imageUrl)
                        .resize(420,300).into(shareDialogImageView);
            }

            TextView shareDialogNewsTitleTextView = shareDialogLayout.findViewById(R.id.share_dialog_news_title);
            shareDialogNewsTitleTextView.setText(topNewsDescriptionTextView.getText().toString());

            ImageButton twitterShareButton = shareDialogLayout.findViewById(R.id.twitter_share_btn);
            twitterShareButton.setOnClickListener(v12 -> {
                String message = "Check out this Link: " + newsUrl;
                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_TEXT, message);
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(String.format(Constants.TWITTER_URL, urlEncode(message))));
                itemView.getContext().startActivity(intent);
            });

            ImageButton chromeViewButton = shareDialogLayout.findViewById(R.id.chrome_view_btn);
            chromeViewButton.setOnClickListener(v1 -> {
                if (newsUrl != null && newsUrl.length() > 0 && URLUtil.isValidUrl(newsUrl)) {
                    Uri uri = Uri.parse(newsUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    itemView.getContext().startActivity(intent);
                }
            });

            dialog.show();
            return true;
        });
        topNewsImageView = itemView.findViewById(R.id.topnews_imageview);
        topNewsSourceTextView = itemView.findViewById(R.id.topnews_source_textview);
        topNewsTimeagoTextView = itemView.findViewById(R.id.topnews_timeago_textview);
        topNewsDescriptionTextView = itemView.findViewById(R.id.topnews_description_textview);
    }

    public ImageView getTopNewsImageView() {
        return topNewsImageView;
    }

    public void setTopNewsImageView(ImageView topNewsImageView) {
        this.topNewsImageView = topNewsImageView;
    }

    public TextView getTopNewsSourceTextView() {
        return topNewsSourceTextView;
    }

    public void setTopNewsSourceTextView(TextView topNewsSourceTextView) {
        this.topNewsSourceTextView = topNewsSourceTextView;
    }

    public TextView getTopNewsTimeagoTextView() {
        return topNewsTimeagoTextView;
    }

    public void setTopNewsTimeagoTextView(TextView topNewsTimeagoTextView) {
        this.topNewsTimeagoTextView = topNewsTimeagoTextView;
    }

    public TextView getTopNewsDescriptionTextView() {
        return topNewsDescriptionTextView;
    }

    public void setTopNewsDescriptionTextView(TextView topNewsDescriptionTextView) {
        this.topNewsDescriptionTextView = topNewsDescriptionTextView;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

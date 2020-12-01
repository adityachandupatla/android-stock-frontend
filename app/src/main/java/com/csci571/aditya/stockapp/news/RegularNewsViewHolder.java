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

public class RegularNewsViewHolder extends ViewHolder {
    private ImageView regularNewsImageView;
    private TextView regularNewsSourceTextView;
    private TextView regularNewsTimeagoTextView;
    private TextView regularNewsDescriptionTextView;
    private String newsUrl;
    private String imageUrl;

    public RegularNewsViewHolder(@NonNull View itemView) {
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
            shareDialogNewsTitleTextView.setText(regularNewsDescriptionTextView.getText().toString());

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
        regularNewsImageView = itemView.findViewById(R.id.regularnews_imageview);
        regularNewsSourceTextView = itemView.findViewById(R.id.regularnews_source_textview);
        regularNewsTimeagoTextView = itemView.findViewById(R.id.regularnews_timeago_textview);
        regularNewsDescriptionTextView = itemView.findViewById(R.id.regularnews_description_textview);
    }

    public ImageView getRegularNewsImageView() {
        return regularNewsImageView;
    }

    public void setRegularNewsImageView(ImageView regularNewsImageView) {
        this.regularNewsImageView = regularNewsImageView;
    }

    public TextView getRegularNewsSourceTextView() {
        return regularNewsSourceTextView;
    }

    public void setRegularNewsSourceTextView(TextView regularNewsSourceTextView) {
        this.regularNewsSourceTextView = regularNewsSourceTextView;
    }

    public TextView getRegularNewsTimeagoTextView() {
        return regularNewsTimeagoTextView;
    }

    public void setRegularNewsTimeagoTextView(TextView regularNewsTimeagoTextView) {
        this.regularNewsTimeagoTextView = regularNewsTimeagoTextView;
    }

    public TextView getRegularNewsDescriptionTextView() {
        return regularNewsDescriptionTextView;
    }

    public void setRegularNewsDescriptionTextView(TextView regularNewsDescriptionTextView) {
        this.regularNewsDescriptionTextView = regularNewsDescriptionTextView;
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

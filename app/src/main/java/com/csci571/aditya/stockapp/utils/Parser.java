package com.csci571.aditya.stockapp.utils;

import android.util.Log;

import com.csci571.aditya.stockapp.models.ArticleModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Parser {

    private static final String TAG = "com.csci571.aditya.stockapp.utils.Parser";

    public static String beautify(double val) {
        DecimalFormat formatter = new DecimalFormat("#,##0.00");
        return formatter.format(val);
    }

    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Unable to URL encode the string: " + s + " UTF-8 should always be supported", e);
            return "";
        }
    }

    public static String getTimeAgo(ArticleModel articleModel, int position) {
        if (articleModel == null || articleModel.getDate() == null || articleModel.getTitle() == null) {
            Log.e(TAG, "Cannot create timeAgo string with null values in: " + ArticleModel.class.getName());
        }
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        String timeAgo = "";
        try {
            Date publishedDate = input.parse(articleModel.getDate());
            Date today = new Date();
            long diffInMilliseconds = today.getTime() - publishedDate.getTime();
            if (diffInMilliseconds < 0) {
                Log.e(TAG, "Published date of article: " + articleModel.getTitle() + " is greater than current date!");
                return timeAgo;
            }
            if (diffInMilliseconds < 3600000) {
                int mins = (int) (diffInMilliseconds / 60000);
                timeAgo = mins + " mins ago";
            }
            else if (diffInMilliseconds < 86400000) {
                int hours = (int) (diffInMilliseconds / 3600000);
                timeAgo = hours + " hours ago";
            }
            else {
                int days = (int) (diffInMilliseconds / 86400000);
                timeAgo = days + " days ago";
            }
        } catch (ParseException e) {
            Log.e(TAG, "Unable to parse date for article at position: "
                    + position + " with title: " + articleModel.getTitle());
        }
        return timeAgo;
    }

}

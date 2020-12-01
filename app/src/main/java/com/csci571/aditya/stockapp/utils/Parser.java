package com.csci571.aditya.stockapp.utils;

import android.util.Log;

import com.csci571.aditya.stockapp.models.ArticleModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Parser {

    private static final String TAG = "com.csci571.aditya.stockapp.utils.Parser";

    public static String beautify(double val) {
        // TODO: Need to round off up to two decimal places (not EXACTLY two)
        double roundOff = Math.round(val * 100.0) / 100.0;
        return String.valueOf(roundOff);
    }

    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.i(TAG, "Unable to URL encode the string: " + s + " UTF-8 should always be supported", e);
            return "";
        }
    }

    public static String getTimeAgo(ArticleModel articleModel, int position) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String timeAgo = "";
        try {
            Date publishedDate = input.parse(articleModel.getDate());
            Date today = new Date();
            long diffInMilliseconds = today.getTime() - publishedDate.getTime();
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
            Log.i(TAG, "Unable to parse date for article at position: "
                    + position + " with title: " + articleModel.getTitle());
        }
        return timeAgo;
    }

}

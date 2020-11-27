package com.csci571.aditya.stockapp.utils;

public class Parser {
    public static String beautify(double val) {
        // TODO: Need to round off up to two decimal places (not EXACTLY two)
        double roundOff = Math.round(val * 100.0) / 100.0;
        return String.valueOf(roundOff);
    }
}

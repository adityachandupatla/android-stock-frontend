package com.csci571.aditya.stockapp.search;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;

public class SearchTextWatcher implements TextWatcher {

    private final int TRIGGER_AUTO_COMPLETE;
    private final long AUTO_COMPLETE_DELAY;
    private final Handler handler;

    public SearchTextWatcher(int TRIGGER_AUTO_COMPLETE, long AUTO_COMPLETE_DELAY, Handler handler) {
        this.TRIGGER_AUTO_COMPLETE = TRIGGER_AUTO_COMPLETE;
        this.AUTO_COMPLETE_DELAY = AUTO_COMPLETE_DELAY;
        this.handler = handler;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s != null && s.toString().length() >= 3) {
            // notify handler only when characters are >= 3
            handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                    AUTO_COMPLETE_DELAY);
        }
    }
}

package com.csci571.aditya.stockapp.favorite;

import android.text.Html;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import com.csci571.aditya.stockapp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

final class FooterViewHolder extends RecyclerView.ViewHolder {

    public FooterViewHolder(@NonNull final View view) {
        super(view);

        ((TextView) view.findViewById(R.id.tiingo_footer)).setMovementMethod(LinkMovementMethod.getInstance());

        Spannable s = (Spannable) Html.fromHtml(view.getResources().getString(R.string.tiingo_linktext), 0);
        for (URLSpan u: s.getSpans(0, s.length(), URLSpan.class)) {
            s.setSpan(new UnderlineSpan() {
                public void updateDrawState(TextPaint tp) {
                    tp.setUnderlineText(false);
                }
            }, s.getSpanStart(u), s.getSpanEnd(u), 0);
        }
        ((TextView) view.findViewById(R.id.tiingo_footer)).setText(s);
    }
}

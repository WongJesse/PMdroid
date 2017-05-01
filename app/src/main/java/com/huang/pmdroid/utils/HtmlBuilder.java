package com.huang.pmdroid.utils;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

/**
 * Created by huang on 2017/4/22.
 *
 */
public class HtmlBuilder {
    private StringBuilder mStringBuilder;

    public HtmlBuilder() {
        mStringBuilder = new StringBuilder();
    }

    public HtmlBuilder appendBold(String bold) {
        mStringBuilder.append(String.format("<b>%s</b>", bold));
        return this;
    }

    public HtmlBuilder appendItalic(String italic) {
        mStringBuilder.append(String.format("<i>%s</i>", italic));
        return this;
    }

    public HtmlBuilder appendTab(int times) {
        for (int i = 0; i < times * 2; i++) {
            mStringBuilder.append("&nbsp;");
        }
        return this;
    }

    public HtmlBuilder appendText(String text) {
        mStringBuilder.append(text);
        return this;
    }

    public HtmlBuilder newLine() {
        return newLine(1);
    }

    public HtmlBuilder newLine(int lines) {
        for (int i = 0; i < lines; i++) {
            mStringBuilder.append("<br/>");
        }
        return this;
    }

    public HtmlBuilder blockquote(HtmlBuilder htmlBuilder) {
        mStringBuilder.append(String.format("<blockquote>%s</blockquote>", htmlBuilder.getRaw()));
        return this;
    }

    @SuppressWarnings("deprecation")
    public Spanned build(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return Html.fromHtml(mStringBuilder.toString(),Html.FROM_HTML_MODE_LEGACY);
        }else{
            return Html.fromHtml(mStringBuilder.toString());
        }
    }


    public String getRaw() {
        return mStringBuilder.toString();
    }
}

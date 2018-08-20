package com.zxycloud.hzy_xg.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

/**
 * @author leiming
 * @date 2017/10/11
 */
public class TxtUtils {
    public static String getText(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static String getText(TextView textText) {
        return textText.getText().toString().trim();
    }

    public static String getText(String text) {
        return TextUtils.isEmpty(text) ? "-" : text;
    }

    public static String getText(int text) {
        return TextUtils.isEmpty(String.format(Locale.CHINA, "%d", text)) ? "-" : String.format(Locale.CHINA, "%d", text);
    }

    public static String getText(long text) {
        return TextUtils.isEmpty(String.format(Locale.CHINA, "%d", text)) ? "-" : String.format(Locale.CHINA, "%d", text);
    }

    public static String getText(double text) {
        return TextUtils.isEmpty(String.format("%s", text)) ? "-" : String.format("%s", text);
    }

    public static String getText(Context context, int resourceId) {
        return context.getResources().getString(resourceId);
    }

    /**
     * 设置文字颜色
     * @param textView
     * @param color
     */
    public static void setTextColorForeground(TextView textView,int color,int start,int end){
        SpannableStringBuilder builder = new SpannableStringBuilder(textView.getText().toString());
        ForegroundColorSpan foreSpan = new ForegroundColorSpan(color);
        builder.setSpan(foreSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }

    /**
     * 设置文字背景色
     * @param textView
     * @param color
     */
    public static void setTextColorBackground(TextView textView,int color,int start,int end){
        SpannableStringBuilder builder = new SpannableStringBuilder(textView.getText().toString());
        BackgroundColorSpan backSpan = new BackgroundColorSpan(color);
        builder.setSpan(backSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }
}

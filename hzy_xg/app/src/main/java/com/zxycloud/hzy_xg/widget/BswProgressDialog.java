package com.zxycloud.hzy_xg.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.widget.TextView;

import com.zxycloud.hzy_xg.R;

/**
 * 网络加载进度条
 *
 * @author leiming
 * @date 2018/4/20 13:58
 */
public class BswProgressDialog extends ProgressDialog {
    private String message = "";
    private Context context;

    @SuppressWarnings("unused")
    public BswProgressDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @SuppressWarnings("unused")
    public BswProgressDialog(Context context) {
        super(context);
        this.context = context;
    }

    @SuppressWarnings("unused")
    public BswProgressDialog(Context context, @StyleRes int theme,
                             String message) {
        super(context, theme);
        this.message = message;
        this.context = context;
    }

    public BswProgressDialog(Context context, @StyleRes int theme,
                             @StringRes int messageResId) {
        super(context, theme);
        this.message = context.getResources().getString(messageResId);
    }

    public void setMessage(String message) {
        ((TextView) findViewById(R.id.progress_text)).setText(message);
    }

    public void setMessage(@StringRes int messageResId) {
        ((TextView) findViewById(R.id.progress_text)).setText(context.getResources().getString(messageResId));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laod_progressbar_layout);
        //dialog弹出后点击物理返回键Dialog消失，但是点击屏幕不会消失
        this.setCanceledOnTouchOutside(false);

        //dialog弹出后点击屏幕或物理返回键，dialog都不消失
        //this.setCancelable(false);
        if (message != null) {
            ((TextView) findViewById(R.id.progress_text)).setText(message);
        }
    }
}
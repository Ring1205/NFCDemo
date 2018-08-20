package com.zxycloud.hzy_xg.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.utils.Const;
import com.zxycloud.hzy_xg.utils.Logger;


/**
 * @author leiming
 * @date 2017/10/11
 */
public class CustomProgressDialog extends ProgressDialog {
    private String message = "";
    private Context context;
    private TextView textView;

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    public CustomProgressDialog(Context context) {
        super(context);
        this.context = context;
    }

    public CustomProgressDialog(Context context, int theme,
                                String message) {
        super(context, theme);
        this.message = message;
        this.context = context;
    }

    public CustomProgressDialog(Context context, int theme,
                                int messageResId) {
        super(context, theme);
        this.context = context;
        this.message = context.getResources().getString(messageResId);
    }

    public void setMessage(String message) {
        if (Const.notEmpty(textView)) {
            textView.setText(message);
        }
    }

    public void setMessage(int messageResId) {
        if (Const.notEmpty(textView)) {
            textView.setText(context.getResources().getString(messageResId));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laod_progressbar_layout);
        //dialog弹出后点击物理返回键Dialog消失，但是点击屏幕不会消失
        this.setCanceledOnTouchOutside(false);

        //dialog弹出后点击屏幕或物理返回键，dialog都不消失
        //this.setCancelable(false);
        try {
            if (message != null) {
                textView = findViewById(R.id.progress_text);
                textView.setText(message);
            }
        } catch (Exception e) {
            Logger.e(getClass().getSimpleName(), e);
        }
    }
}
package com.zxycloud.hzy_xg.utils.KeepAlive;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.utils.JumpToUtils;
import com.zxycloud.hzy_xg.utils.Logger;


public class KeepAliveActivity extends AppCompatActivity {

    public static final String TAG = KeepAliveActivity.class.getSimpleName();

    public static void actionToSinglePixelActivity(Context pContext) {
        JumpToUtils.receiverJumpTo(pContext, KeepAliveActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keep_alive);

        findViewById(R.id.keep_alive_bg).setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                finish();
                return false;
            }
        });

        if (!ScreenBroadcastListener.isScreenLocked) {
            finish();
        }

        Logger.i("ScreenBroadcastReceiver", "onCreate");

        ScreenManager.getInstance(this).setActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

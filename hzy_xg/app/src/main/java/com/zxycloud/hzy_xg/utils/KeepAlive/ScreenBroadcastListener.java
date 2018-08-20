package com.zxycloud.hzy_xg.utils.KeepAlive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zxycloud.hzy_xg.App;
import com.zxycloud.hzy_xg.utils.JumpToUtils;
import com.zxycloud.hzy_xg.utils.Logger;

/**
 * @author leiming
 * @date 2018/7/23.
 */

public class ScreenBroadcastListener {
    public static boolean isScreenLocked = false;
    private Context mContext;

    private ScreenBroadcastReceiver mScreenReceiver;

    private ScreenStateListener mListener;

    public ScreenBroadcastListener(Context context) {
        mContext = context.getApplicationContext();
        mScreenReceiver = new ScreenBroadcastReceiver();
    }

    public interface ScreenStateListener {

        void onScreenOn();

        void onScreenOff();
    }

    /**
     * screen状态广播接收者
     */
    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        private String action = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏
//                mListener.onScreenOn();
                Logger.i(getClass().getSimpleName(), "activity judge");
                App.finish(KeepAliveActivity.class);
                Logger.i(getClass().getSimpleName(), "activity judge2");
                isScreenLocked = false;
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
//                mListener.onScreenOff();
                Logger.i(getClass().getSimpleName(), "activity open");
                JumpToUtils.receiverJumpTo(context, KeepAliveActivity.class);
                isScreenLocked = true;
            }
        }
    }

    public void registerListener(ScreenStateListener listener) {
        mListener = listener;
        registerListener();
    }

    private void registerListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mContext.registerReceiver(mScreenReceiver, filter);
    }
}

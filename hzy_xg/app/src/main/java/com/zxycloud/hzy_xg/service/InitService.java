package com.zxycloud.hzy_xg.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.zxycloud.hzy_xg.App;
import com.zxycloud.hzy_xg.ui.activity.MainActivity;
import com.zxycloud.hzy_xg.utils.Logger;
import com.zxycloud.hzy_xg.utils.TimerUtils;

/**
 * 数据初始化服务
 *
 * @author leiming
 * @date 2017/10/11
 */
public class InitService extends Service implements TimerUtils.OnBaseTimerCallBack {

    /**
     * 定时器工具
     */
    private TimerUtils timerUtils;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        timerUtils = new TimerUtils(1000 * 60 * 10, 1000, this);
        timerUtils.start();
    }

    @Override
    public void onTick(long millisUntilFinished) {
        // 获取极光ID
        String registrationId = App.getInstance().getRegistrationId();
        if (!TextUtils.isEmpty(registrationId)) {
            Logger.i("MyReceiver", String.format("%s*******************", registrationId));
            if (MainActivity.mainActivity != null){
                MainActivity.mainActivity.setPushId();
            }
            timerUtils.stop();
            this.stopSelf();
        }
    }

    @Override
    public void onFinish() {

    }
}

package com.zxycloud.hzy_xg;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.TextUtils;

import com.zxycloud.hzy_xg.ui.activity.SplashActivity;
import com.zxycloud.hzy_xg.utils.Logger;
import com.zxycloud.hzy_xg.utils.SPUtils;
import com.zxycloud.hzy_xg.widget.CrashHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.jpush.android.api.JPushInterface;

/**
 * @author leiming
 * @date 2017/10/11
 */
public class App extends Application {

    private static App app;
    private String registrationId;
    private String androidId;

    public int foreActivity = 0;
    public static CrashHandler crashHandler;

    /**
     * 当前打开Activity存储List
     */
    public static List<Activity> activities = new ArrayList<>();

    public static App getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 异常处理，不需要处理时注释掉这两句即可！
//        crashHandler = CrashHandler.getInstance();
        // 注册crashHandler
//        crashHandler.init(getApplicationContext(), new CrashHandler.CustomExceptionHandler() {
//            @Override
//            public void handlerException(Thread thread, Throwable throwable) {
//                Logger.e("CrashHandler=====", throwable);
//            }
//        });

        // 极光推送初始化
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        app = this;

        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacksImpl());
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    @SuppressLint("HardwareIds")
    public String getAndroidId() {
        if (TextUtils.isEmpty(androidId)) {
            androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            Logger.i("androidId", androidId);
        }
        return androidId;
    }
    public String getRegistrationId() {
        if (TextUtils.isEmpty(registrationId)) {
            registrationId = JPushInterface.getRegistrationID(getApplicationContext());
        }
        return registrationId;
    }
    /**
     * 获取内部存储路径
     *
     * @return 路径
     */
    public String getDirPath() {
        return getApplicationContext().getFilesDir().getAbsolutePath();
    }

    private class ActivityLifecycleCallbacksImpl implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            activities.add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            foreActivity++;
            Logger.i("activityNum", String.format(Locale.CHINA, "onActivityStarted  openNum = %d", foreActivity));
            if (activity instanceof SplashActivity) {
                SPUtils.getInstance(getApplicationContext()).put(SPUtils.RESTART, 0L);
                return;
            }
            // 后台10分钟重启APP
//            long restartTime = SPUtils.getInstance(getApplicationContext()).getLong(SPUtils.RESTART, 0L);
//            if (restartTime != 0L) {
//                if (new Date().getTime() - restartTime > 10 * 60 * 1000) {
//                    if (activity instanceof BaseActivity) {
//                        ((BaseActivity) activity).finishActivity();
//                    }
//                    restartApplication();
//                }
//                SPUtils.getInstance(getApplicationContext()).put(SPUtils.RESTART, 0L);
//            }
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            foreActivity--;
            Logger.i("activityNum", String.format(Locale.CHINA, "onActivityStopped  openNum = %d", foreActivity));
            if (foreActivity == 0) {
                SPUtils.getInstance(getApplicationContext()).put(SPUtils.RESTART, new Date().getTime());
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            activities.remove(activity);
            Logger.i("alertFinish", "onActivityDestroyed");
        }

        private void restartApplication() {
            final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    public static void finish(Class<?> targetActivity) {
        int size = activities.size();
        for (int i = size - 1; i >= 0; i--) {
            if (targetActivity.equals(activities.get(i).getClass())) {
                activities.get(i).finish();
                break;
            }
        }
    }

    public static boolean hasActivity(Class clazz) {
        for (Activity activity1 : activities) {
            if (clazz.equals(activity1.getClass())) {
                boolean isActivityAlive = ! activity1.isFinishing();
                if (! isActivityAlive) {
                    activities.remove(activity1);
                }
                return isActivityAlive;
            }
        }
        return false;
    }
}

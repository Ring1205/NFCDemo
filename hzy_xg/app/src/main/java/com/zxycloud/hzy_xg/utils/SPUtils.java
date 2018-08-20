package com.zxycloud.hzy_xg.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.zxycloud.hzy_xg.ui.activity.MainActivity;

import java.util.Map;
import java.util.Set;

/**
 * SharedPreference工具类
 *
 * @author leiming
 * @date 2018/1/5
 */
public class SPUtils {
    /**
     * 开始添加场所的页面
     */
    public static final String ADD_PLACE_FROM = "addPlaceFrom";
    /**
     * 开始添加设备的页面
     */
    public static final String ADD_DEVICE_FROM = "addDeviceFrom";
    /**
     * 未正常退出时的token存储(暂未使用)
     */
    public static final String OLD_TOKEN = "oldToken";
    /**
     * 当前用户Token
     */
    public static final String USER_TOKEN = "UserToken";
    /**
     * 当前用户账号
     */
    public static final String USER_ACCOUNT = "UserAccount";
    /**
     * 当前用户账号
     */
    public static final String USER_ID = "UserId";
    /**
     * 当前项目id
     */
    public static final String PROJECT_ID = "ProjectId";
    /**
     * 当前用户密码
     */
    public static final String USER_PASSWORD = "UserPassword";
    /**
     * 是否记住用户密码
     */
    public static final String USER_PASSWORD_REMEMBER = "userPasswordRemember";
    /**
     * 用户手机号
     */
    public static final String USER_PHONE = "UserPhone";
    /**
     * 用户昵称
     */
    public static final String USER_NAME = "UserName";

    /**
     * SSO
     */
    public final static String SSO = "sso";
    /**
     * 权限
     */
    public final static String PERMISSION = "permission";
    /**
     * 巡更
     */
    public final static String PATROL = "patrol";
    /**
     * 文件上传
     */
    public static final String UPLOAD = "upload";
    /**
     * 错误日志上传
     */
    public static final String LOG = "log";

    public static final String ERROR = "error";

    /**
     * 来火警推送时，返回首页页面
     */
    public static final String FIRE_PUSH = "firePush";
    /**
     * APP长时间后台运行重启
     */
    public static final String RESTART = "restartTime";
    /**
     * 火警推送是否开启推送
     */
    public static final String ALERT_ID_SET = "alertIdSet";
    /**
     * 是否有场所
     */
    public static final String HAS_ADDRESS = "hasAddress";
    /**
     * 首次进入APP获取权限
     */
    public static final String INIT_PERMISSION = "init_permission";
    /**
     * 首次进入APP展示首页引导
     */
    public static final String INIT_HOMEPAGE_GUIDE = "init_homepage_guide";

    /**
     * 服务器地址
     */
    public static final String IP_URL = "ip_url";
    /**
     * 服务器端口号
     */
    public static final String IP_PORT = "ip_port";
    /**
     * 服务器是否使用SSL加密
     */
    public static final String IP_HTTPS = "ip_https";

    private SharedPreferences preferences = null;
    private static SPUtils utils;

    public SPUtils(Context context) {
        initPreferences(context);
    }

    public static SPUtils getInstance(Context context) {
        if (utils == null) {
            utils = new SPUtils(context);
        }
        return utils;
    }

    private void initPreferences(Context context) {
        if (preferences == null) {
            preferences = context.getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        }
    }

    public SPUtils put(String key, String value) {
        preferences.edit().putString(key, value).apply();
        return utils;
    }

    public SPUtils put(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
        return utils;
    }

    public SPUtils put(String key, int value) {
        preferences.edit().putInt(key, value).apply();
        return utils;
    }

    public SPUtils put(String key, Activity activity) {
        preferences.edit().putString(key, activity.getClass().getName()).apply();
        return utils;
    }

    public SPUtils put(String key, Class clazz) {
        preferences.edit().putString(key, clazz.getName()).apply();
        return utils;
    }

    public SPUtils put(String key, float value) {
        preferences.edit().putFloat(key, value).apply();
        return utils;
    }

    public SPUtils put(String key, long value) {
        preferences.edit().putLong(key, value).apply();
        return utils;
    }

    public SPUtils put(String key, Set<String> values) {
        preferences.edit().putStringSet(key, values).apply();
        return utils;
    }

    public String getString(String key, String defaultString) {
        return preferences.getString(key, defaultString);
    }

    public String getString(String key) {
        return preferences.getString(key, "");
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultBoolean) {
        return preferences.getBoolean(key, defaultBoolean);
    }

    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    public int getInt(String key, int defaultInt) {
        return preferences.getInt(key, defaultInt);
    }

    public float getFloat(String key, float defaultFloat) {
        return preferences.getFloat(key, defaultFloat);
    }

    public Class getClass(String key) throws ClassNotFoundException {
        // 存储Class实际存储的就是String，所以这里需要先提取String，再转为Class
        return Class.forName(preferences.getString(key, MainActivity.class.getName()));
    }

    public float getFloat(String key) {
        return preferences.getFloat(key, 0);
    }

    public long getLong(String key) {
        return preferences.getLong(key, 0);
    }

    public long getLong(String key, long defaultLong) {
        return preferences.getLong(key, defaultLong);
    }

    public Set<String> getStringSet(String key) {
        return preferences.getStringSet(key, null);
    }

    public Set<String> getStringSet(String key, Set<String> defaultStringSet) {
        return preferences.getStringSet(key, defaultStringSet);
    }

    public Map<String, ?> getStringSet() {
        return preferences.getAll();
    }

    public void remove(String key) {
        preferences.edit().remove(key).apply();
    }

    public void clear() {
        preferences.edit().clear().apply();
    }
}

package com.zxycloud.hzy_xg.base;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.zxycloud.hzy_xg.BuildConfig;
import com.zxycloud.hzy_xg.R;

import java.io.Serializable;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */

public class BaseBean implements Serializable {
    /**
     * 返回值code 1000：请求成功 9999：他人登录，需重新登录 7777：异常
     */
    private String code;

    /**
     * 返回信息
     */
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage(){
        return TextUtils.isEmpty(message) ? "message为null" : message;
    }
    public String getMessage(Context context) {
        return TextUtils.isEmpty(message) ? context.getResources().getString(isSuccessful() ? R.string.operation_success : R.string.operation_error) : message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccessful() {
        return ! TextUtils.isEmpty(code) && code.equals(BuildConfig.isSuccessful);
    }

    /**
     * 异地登录判断
     *
     * @return 是否需要登出
     */
    public boolean isLogout() {
        return TextUtils.isEmpty(code) || code.equals(BuildConfig.logOutCode1) || code.equals(BuildConfig.logOutCode2) || code.equals(BuildConfig.logOutCode3);
    }

    /**
     * 获取当前Bean对象类名
     *
     * @return 类名字符串
     */
    protected String getName() {
        return getClass().getSimpleName();
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}

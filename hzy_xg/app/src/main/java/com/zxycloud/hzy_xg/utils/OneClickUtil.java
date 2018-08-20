package com.zxycloud.hzy_xg.utils;

import java.util.Calendar;

/**
 * @author leiming
 * @date 2017/10/11
 */
public class OneClickUtil {
    private String methodName;
    private int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    public OneClickUtil(String methodName) {
        this.methodName = methodName;
    }

    public OneClickUtil(int MIN_CLICK_DELAY_TIME) {
        this.MIN_CLICK_DELAY_TIME = MIN_CLICK_DELAY_TIME;
    }

    public String getMethodName() {
        return methodName;
    }

    public void reset() {
        lastClickTime = 0;
    }

    public boolean doubleCheck() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            return false;
        } else {
            return true;
        }
    }

    public boolean Check() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (lastClickTime == 0) {
            lastClickTime = currentTime;
            return false;
        }
        return currentTime - lastClickTime > MIN_CLICK_DELAY_TIME;
    }
}

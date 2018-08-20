package com.zxycloud.hzy_xg.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author leiming
 * @date 2017/10/11
 */
public class Const {
    /**
     * 登录超时
     */
    public static boolean isLogoutTime = false;

    /**
     * 监听只能输入汉字
     */
    public static InputFilter chineseFilter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            if (source.equals(".")) {
                return "";
            }
            for (int i = start; i < end; i++) {
                if (! isChinese(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        }
    };

//    /**
//     * 监听禁止输入非法字符，不禁止“ ' ”，防止华为输入法、搜狗输入法无法正常输入
//     */
//    public static InputFilter noIllegalCharacterFilter = new InputFilter() {
//        public CharSequence filter(CharSequence source, int start, int end,
//                                   Spanned dest, int dstart, int dend) {
//            String speChat = "[`~!$%^&*+=|{}:;,\\[\\].<>/?~！@#￥%……&*——+|{}【】‘；：”“’、？]";
//            Pattern pattern = Pattern.compile(speChat);
//            Matcher matcher = pattern.matcher(source.toString());
//            if (matcher.find()) return "";
//            else return null;
//        }
//    };

    /**
     * 监听输入座机或手机号允许字段
     */
    public static InputFilter isPhoneNum = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (! isPhoneNumMatch(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        }
    };

    /**
     * 极光相关参数
     */
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static boolean isForeground = false;

    /**
     * 判断集合的长度
     *
     * @param list 索要获取长度的集合
     * @return 该集合的长度
     */
    public static int judgeListNull(List list) {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    /**
     * 判断数组的长度
     *
     * @param list 数组
     * @param <T>  数组元素
     * @return 数组长度
     */
    public static <T extends Object> int judgeListNull(T[] list) {
        if (list == null) {
            return 0;
        } else {
            return list.length;
        }
    }

    /**
     * 当前对象为空判断
     *
     * @param o 被判断对象
     * @return 是否为空
     */
    public static boolean isEmpty(Object o) {
        return null == o;
    }

    /**
     * 当前对象存在判断
     *
     * @param o 被判断对象
     * @return 是否存在
     */
    public static boolean notEmpty(Object o) {
        return null != o;
    }

    private static boolean isPhoneNumMatch(char c) {
        String regExp = "^[0-9]|-| *$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(String.format("%s", c));
        return m.matches();
    }

    /**
     * 判定输入汉字
     *
     * @param c 字符串单个字符
     * @return 是否为汉字
     */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        return ! TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName());
    }

    /**
     * 判断服务是否启动
     *
     * @param mContext 上下文对象
     * @param clazz    服务的类
     * @return 是否启动
     */
    public static boolean isServiceRunning(Context mContext, Class clazz) {

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        if (Const.notEmpty(activityManager)) {
            List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                    .getRunningServices(30);

            if (! (serviceList.size() > 0)) {
                return false;
            }

            for (int i = 0; i < serviceList.size(); i++) {
                if (serviceList.get(i).service.getClassName().equals(clazz.getName())) {
                    isRunning = true;
                    break;
                }
            }
        }
        return isRunning;
    }

    /**
     * 判断服务是否启动
     *
     * @param mContext  上下文对象
     * @param className 服务的name
     * @return 是否启动
     */
    public static boolean isServiceRunning(Context mContext, String className) {

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        if (Const.notEmpty(activityManager)) {
            List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                    .getRunningServices(30);

            if (! (serviceList.size() > 0)) {
                return false;
            }

            for (int i = 0; i < serviceList.size(); i++) {
                if (serviceList.get(i).service.getClassName().equals(className)) {
                    isRunning = true;
                    break;
                }
            }
        }
        return isRunning;
    }

    /**
     * 获取类名
     *
     * @param clz 需要获取名称的类
     * @return 类名字符串
     */
    public static String getName(Class<?> clz) {
        return clz.getClass().getSimpleName();
    }
}

package com.zxycloud.hzy_xg.utils;

import android.support.annotation.NonNull;

/**
 * @author leiming
 * @date 2018/6/30.
 */

public class SplitUnionIdUtils {
    /**
     * 拆分deviceUnionId
     *
     * @param deviceUnionId 待拆分Id
     * @return 拆分得到的字符串数组：0.项目ID；1.设备ID
     */
    public static String[] split(@NonNull String deviceUnionId) {
        return deviceUnionId.replace("@", "").split("\\$");
    }
}

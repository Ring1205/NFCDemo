package com.zxycloud.hzy_xg.utils;

/**
 * @author leiming
 * @date 2017/10/11
 */
public class BooleanUtils {
    /**
     * @param bool1 bool判断位1
     * @param bool2 bool判断位2
     * @return 同或，相同为true，不同为false
     */
    private static boolean xnor(boolean bool1, boolean bool2) {
        return bool1 && bool2 || (!bool1 && !bool2);
    }

    /**
     * @param bool1 bool判断位1
     * @param bool2 bool判断位2
     * @return 异或，相同为false，不同为true
     */
    public static boolean xor(boolean bool1, boolean bool2) {
        return !xnor(bool1, bool2);
    }
}

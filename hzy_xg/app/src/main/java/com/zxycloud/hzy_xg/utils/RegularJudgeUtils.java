package com.zxycloud.hzy_xg.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author leiming
 * @date 2017/10/11
 */
public class RegularJudgeUtils {
    public static boolean isChinaMobilephoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^1[0-9]{10}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^1\\d{10}$|^(0\\d{2,3}(-| |)?|\\(0\\d{2,3}\\))[1-9]\\d{4,7}((-| |)\\d{1,8})?$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static boolean isMailLegal(String str) throws PatternSyntaxException {
        String regExp = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}

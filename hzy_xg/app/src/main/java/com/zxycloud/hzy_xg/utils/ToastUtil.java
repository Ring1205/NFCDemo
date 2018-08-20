package com.zxycloud.hzy_xg.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    public static void showShortToast(Context context,String toast){
        Toast.makeText(context,toast,Toast.LENGTH_SHORT).show();
    }
    public static void showLongToast(Context context,String toast){
        Toast.makeText(context,toast,Toast.LENGTH_LONG).show();
    }
}

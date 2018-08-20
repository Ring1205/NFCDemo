package com.zxycloud.hzy_xg.bean.base;

import android.support.annotation.DrawableRes;

/**
 * @author leiming
 * @date 2018/6/21.
 */

public class ImageAddBean {
    private int imgResId;
    private String imgPath;

    public ImageAddBean(@DrawableRes int imgResId) {
        this.imgResId = imgResId;
    }

    public ImageAddBean(String imgPath) {
        this.imgPath = imgPath;
    }

    public int getImgResId() {
        return imgResId;
    }

    public String getImgPath() {
        return imgPath;
    }
}

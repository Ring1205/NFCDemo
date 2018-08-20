package com.zxycloud.hzy_xg.bean.base;

import java.io.Serializable;

/**
 * @author leiming
 * @date 2018/6/23.
 */

public class ImageUploadItemBean implements Serializable {
    private String imgUrl;
    private String imgThumbUrl;
    private int imgWidth;
    private int imgHeight;

    public ImageUploadItemBean(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgThumbUrl() {
        return imgThumbUrl;
    }

    public void setImgThumbUrl(String imgThumbUrl) {
        this.imgThumbUrl = imgThumbUrl;
    }

    public int getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(int imgWidth) {
        this.imgWidth = imgWidth;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public void setImgHeight(int imgHeight) {
        this.imgHeight = imgHeight;
    }
}

package com.zxycloud.hzy_xg.bean.base;

import java.util.List;

/**
 * @author leiming
 * @date 2018/6/23.
 */

public class ImageUploadResultBean {
    private List<ImageUploadItemBean> fileURL;

    public List<ImageUploadItemBean> getFileURL() {
        return fileURL;
    }

    public void setFileURL(List<ImageUploadItemBean> fileURL) {
        this.fileURL = fileURL;
    }
}

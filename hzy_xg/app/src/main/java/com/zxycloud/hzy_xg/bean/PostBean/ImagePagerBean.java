package com.zxycloud.hzy_xg.bean.PostBean;

import com.zxycloud.hzy_xg.bean.base.ImageUploadItemBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author leiming
 * @date 2018/6/26.
 */

public class ImagePagerBean implements Serializable {
    private List<ImageUploadItemBean> images;

    public ImagePagerBean(List<ImageUploadItemBean> images) {
        this.images = images;
    }

    public List<ImageUploadItemBean> getImages() {
        return images;
    }

    public void setImages(List<ImageUploadItemBean> images) {
        this.images = images;
    }
}

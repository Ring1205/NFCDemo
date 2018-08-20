package com.zxycloud.hzy_xg.bean.base;

/**
 * @author leiming
 * @date 2018/6/23.
 */

public class VideoUploadItemBean {
    private String videoUrl;

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public VideoUploadItemBean(String videoUrl){
        this.videoUrl = videoUrl;
    }
}

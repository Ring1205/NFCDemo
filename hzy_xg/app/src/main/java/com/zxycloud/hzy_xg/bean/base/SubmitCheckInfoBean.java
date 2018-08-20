package com.zxycloud.hzy_xg.bean.base;

import com.zxycloud.hzy_xg.utils.Const;

import java.util.List;

/**
 * @author leiming
 * @date 2018/6/23.
 */

public class SubmitCheckInfoBean {
    private boolean isAdded = false;
    private List<ImageUploadItemBean> images;
    private List<VideoUploadItemBean> videos;
    private List<RecordingUploadItemBean> recording;
    private String sumbitDescripe;
    private double lng;
    private double lat;

    public List<ImageUploadItemBean> getImages() {
        return images;
    }

    public void setImages(List<ImageUploadItemBean> images) {
        if (Const.judgeListNull(images) != 0) {
            isAdded = true;
        }
        this.images = images;
    }

    public List<VideoUploadItemBean> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoUploadItemBean> videos) {
        if (Const.judgeListNull(videos) != 0) {
            isAdded = true;
        }
        this.videos = videos;
    }

    public List<RecordingUploadItemBean> getRecording() {
        return recording;
    }

    public void setRecording(List<RecordingUploadItemBean> recording) {
        if (Const.judgeListNull(recording) != 0) {
            isAdded = true;
        }
        this.recording = recording;
    }

    public String getSumbitDescripe() {
        return sumbitDescripe;
    }

    public void setSumbitDescripe(String sumbitDescripe) {
        isAdded = true;
        this.sumbitDescripe = sumbitDescripe;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public boolean isAdded() {
        return isAdded;
    }
}

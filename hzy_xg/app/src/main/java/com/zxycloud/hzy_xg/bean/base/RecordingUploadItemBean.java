package com.zxycloud.hzy_xg.bean.base;

/**
 * @author leiming
 * @date 2018/6/23.
 */

public class RecordingUploadItemBean {
    private String recordingUrl;

    public RecordingUploadItemBean(String recordingUrl) {
        this.recordingUrl = recordingUrl;
    }

    public String getRecordingUrl() {
        return recordingUrl;
    }

    public void setRecordingUrl(String recordingUrl) {
        this.recordingUrl = recordingUrl;
    }
}

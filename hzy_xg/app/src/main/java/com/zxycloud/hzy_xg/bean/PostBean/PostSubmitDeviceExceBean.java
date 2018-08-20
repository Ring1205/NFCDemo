package com.zxycloud.hzy_xg.bean.PostBean;

import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.bean.base.SubmitCheckInfoBean;

public class PostSubmitDeviceExceBean extends BaseBean {
    /**
     * projectGuid : 34343434
     * taskId : ca9e22febf2f43b982d4fc3a6956c5c2
     * deviceId : dfdfdf
     * faultReason : dfdfdf
     * fileUrls : {xxx:'xxx'}
     */

    private String projectGuid;
    private String taskId;
    private String deviceId;
    private String faultReason;
    private SubmitCheckInfoBean fileUrls;

    public String getProjectGuid() {
        return projectGuid;
    }

    public void setProjectGuid(String projectGuid) {
        this.projectGuid = projectGuid;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getFaultReason() {
        return faultReason;
    }

    public void setFaultReason(String faultReason) {
        this.faultReason = faultReason;
    }
    public SubmitCheckInfoBean getFileUrl() {
        return fileUrls;
    }

    public void setFileUrl(SubmitCheckInfoBean fileUrls) {
        this.fileUrls = fileUrls;
    }

}

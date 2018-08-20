package com.zxycloud.hzy_xg.bean.base;

import android.text.TextUtils;

import com.zxycloud.hzy_xg.base.BaseBean;

/**
 * IP Bean
 *
 * @author leiming
 * @date 2017/10/11
 */
public class IpBean extends BaseBean {
    /**
     * ssoUrl           : String   , 验证接口
     * uploadUrl        : String   , 文件上传
     * applogUrl        : String   , 日志上传
     * permissionUrl    : String   , 权限
     * patrolUrl        : String   , 巡更
     */

    private String ssoUrl;
    private String uploadUrl;
    private String applogUrl;
    private String permissionUrl;
    private String patrolUrl;

    public String getSsoUrl() {
        return ssoUrl;
    }

    public void setSsoUrl(String ssoUrl) {
        this.ssoUrl = ssoUrl;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getApplogUrl() {
        return applogUrl;
    }

    public void setApplogUrl(String applogUrl) {
        this.applogUrl = applogUrl;
    }

    public String getPermissionUrl() {
        return permissionUrl;
    }

    public void setPermissionUrl(String permissionUrl) {
        this.permissionUrl = permissionUrl;
    }

    public String getPatrolUrl() {
        return patrolUrl;
    }

    public void setPatrolUrl(String patrolUrl) {
        this.patrolUrl = patrolUrl;
    }

    public boolean cannotUse() {
        return TextUtils.isEmpty(ssoUrl) ||
                TextUtils.isEmpty(uploadUrl) ||
                TextUtils.isEmpty(applogUrl) ||
                TextUtils.isEmpty(permissionUrl) ||
                TextUtils.isEmpty(patrolUrl);
    }
}

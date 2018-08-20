package com.zxycloud.hzy_xg.bean.GetBean;

import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.bean.base.IpBean;

/**
 * 私有云获取IP Bean
 *
 * @author leiming
 * @date 2017/10/11
 */
public class GetIpBean extends BaseBean {

    /**
     * IP Bean
     */
    private IpBean app;

    public IpBean getApp() {
        return app;
    }

    public void setApp(IpBean app) {
        this.app = app;
    }
}

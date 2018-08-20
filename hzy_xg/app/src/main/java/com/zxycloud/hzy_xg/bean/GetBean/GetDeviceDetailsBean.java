package com.zxycloud.hzy_xg.bean.GetBean;

import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.bean.base.DeviceBean;

public class GetDeviceDetailsBean extends BaseBean {

    private DeviceBean data;
    private Object totalCount;

    public DeviceBean getData() {
        return data;
    }

    public void setData(DeviceBean data) {
        this.data = data;
    }

    public Object getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Object totalCount) {
        this.totalCount = totalCount;
    }
}

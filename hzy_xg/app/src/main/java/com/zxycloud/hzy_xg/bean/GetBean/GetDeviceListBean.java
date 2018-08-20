package com.zxycloud.hzy_xg.bean.GetBean;

import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.bean.base.DeviceBean;

import java.util.List;

public class GetDeviceListBean extends BaseBean {

    private int totalCount;
    private List<DeviceBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<DeviceBean> getData() {
        return data;
    }

    public void setData(List<DeviceBean> data) {
        this.data = data;
    }

}

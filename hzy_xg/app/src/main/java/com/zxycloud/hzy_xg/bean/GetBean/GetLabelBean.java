package com.zxycloud.hzy_xg.bean.GetBean;

import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.bean.base.LabelBean;

import java.util.List;

public class GetLabelBean extends BaseBean {
    private int totalCount;
    private List<LabelBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<LabelBean> getData() {
        return data;
    }

    public void setData(List<LabelBean> data) {
        this.data = data;
    }
}

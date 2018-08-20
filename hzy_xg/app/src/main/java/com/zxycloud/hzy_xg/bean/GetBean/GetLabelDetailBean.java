package com.zxycloud.hzy_xg.bean.GetBean;

import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.bean.base.LabelBean;

public class GetLabelDetailBean extends BaseBean {
    private LabelBean data;
    private Object totalCount;

    public LabelBean getData() {
        return data;
    }

    public void setData(LabelBean data) {
        this.data = data;
    }

    public Object getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Object totalCount) {
        this.totalCount = totalCount;
    }

}

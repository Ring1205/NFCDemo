package com.zxycloud.hzy_xg.bean.GetBean;

import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.bean.base.PlanBean;

import java.util.List;

public class GetPlanListBean extends BaseBean{
    private int totalCount;
    private List<PlanBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<PlanBean> getData() {
        return data;
    }

    public void setData(List<PlanBean> data) {
        this.data = data;
    }
}

package com.zxycloud.hzy_xg.bean.GetBean;

import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.bean.base.TaskBean;

import java.util.List;

public class GetTaskListBean extends BaseBean{

    private int totalCount;
    private List<TaskBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<TaskBean> getData() {
        return data;
    }

    public void setData(List<TaskBean> data) {
        this.data = data;
    }

}

package com.zxycloud.hzy_xg.bean.GetBean;

import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.bean.base.TaskDetailBean;

public class GetTaskDetailBean extends BaseBean {
    private TaskDetailBean data;
    private Object totalCount;

    public TaskDetailBean getData() {
        return data;
    }

    public void setData(TaskDetailBean data) {
        this.data = data;
    }

    public Object getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Object totalCount) {
        this.totalCount = totalCount;
    }
}

package com.zxycloud.hzy_xg.bean.GetBean;

import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.bean.base.TaskBean;

import java.util.List;

public class GetAlarmTaskListBean extends BaseBean {
    /**
     * data : [{"id":"59d206539729481285691d403bcd8995","markId":"9a71de93845449b4a71306473a6679c6","startTime":"2018-08-22 00:00","endTime":"2018-08-24 23:59","patrol":2,"status":1,"remark":"","planId":"be1225af5eab434a8aeca91ad0b9127b","addTime":"2018-08-06 17:13","projectGuid":"6c9d5c0377df4965869e2fa83c3a2791","patrolName":"进行中","statusName":"正常","taskName":"1-59d206539729481285691d403bcd8995","markName":"name"}]
     * totalCount : 1
     */

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

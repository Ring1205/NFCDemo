package com.zxycloud.hzy_xg.bean.GetBean;

import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.bean.base.PlanDetailBean;

public class GetPlanDetailBean extends BaseBean {
    /**
     * data : {"id":"4ab5e9a427d14024aeaf5f51a264de5a","title":"月计划123","describe":"巡检描述1","genTaskType":3,"genTaskCycle":1,"itemHourTime":"18:00","itemMonthTime":"每月2日-每月4日","advancedTimeCall":1,"projectGuid":"6c9d5c0377df4965869e2fa83c3a2791","createTime":"2018-08-06 16:39:49","markNames":["5555","66666"]}
     * totalCount : null
     */

    private PlanDetailBean data;
    private Object totalCount;

    public PlanDetailBean getData() {
        return data;
    }

    public void setData(PlanDetailBean data) {
        this.data = data;
    }

    public Object getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Object totalCount) {
        this.totalCount = totalCount;
    }

}

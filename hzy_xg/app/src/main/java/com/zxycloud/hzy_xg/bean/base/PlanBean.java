package com.zxycloud.hzy_xg.bean.base;

public class PlanBean {
    /**
     * id : 4ab5e9a427d14024aeaf5f51a264de5a
     * title : 月计划
     * describe : 巡检描述1
     * genTaskType : 3
     * startTime : null
     * endTime : null
     * genTaskCycle : 1
     * itemHourTime : 18:00
     * itemMonthTime : 2-4
     * advancedTimeCall : 1
     * projectGuid : 6c9d5c0377df4965869e2fa83c3a2791
     * createTime : 2018-08-06 16:39:49
     */

    private String id;
    private String title;
    private String describe;
    private int genTaskType;
    private Object startTime;
    private Object endTime;
    private int genTaskCycle;
    private String itemHourTime;
    private String itemMonthTime;
    private int advancedTimeCall;
    private String projectGuid;
    private String createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getGenTaskType() {
        return genTaskType;
    }

    public void setGenTaskType(int genTaskType) {
        this.genTaskType = genTaskType;
    }

    public Object getStartTime() {
        return startTime;
    }

    public void setStartTime(Object startTime) {
        this.startTime = startTime;
    }

    public Object getEndTime() {
        return endTime;
    }

    public void setEndTime(Object endTime) {
        this.endTime = endTime;
    }

    public int getGenTaskCycle() {
        return genTaskCycle;
    }

    public void setGenTaskCycle(int genTaskCycle) {
        this.genTaskCycle = genTaskCycle;
    }

    public String getItemHourTime() {
        return itemHourTime;
    }

    public void setItemHourTime(String itemHourTime) {
        this.itemHourTime = itemHourTime;
    }

    public String getItemMonthTime() {
        return itemMonthTime;
    }

    public void setItemMonthTime(String itemMonthTime) {
        this.itemMonthTime = itemMonthTime;
    }

    public int getAdvancedTimeCall() {
        return advancedTimeCall;
    }

    public void setAdvancedTimeCall(int advancedTimeCall) {
        this.advancedTimeCall = advancedTimeCall;
    }

    public String getProjectGuid() {
        return projectGuid;
    }

    public void setProjectGuid(String projectGuid) {
        this.projectGuid = projectGuid;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
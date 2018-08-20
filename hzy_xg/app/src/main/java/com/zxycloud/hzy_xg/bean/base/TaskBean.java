package com.zxycloud.hzy_xg.bean.base;

public class TaskBean {

    /**
     * id : c911de327814462f8efa5470862ad35d
     * markId : 343434343434343
     * startTime : 2018-11-05 00:00:00
     * endTime : 2018-12-05 23:59:59
     * patrol : 1
     * status : 1
     * remark :
     * planId : cb4279adb1d645ef86eb9bde6bc31261
     * addTime : 2018-08-06 21:20:11
     * projectGuid : 6c9d5c0377df4965869e2fa83c3a2791
     * patrolName : 未开始
     * statusName : 正常
     * taskName : 1-59d206539729481285691d403bcd8995
     * markName : name
     */

    private String id;
    private String markId;
    private String startTime;
    private String endTime;
    private int patrol;
    private int status;
    private String remark;
    private String taskName;
    private String planId;
    private String addTime;
    private String projectGuid;
    private String patrolName;
    private String statusName;
    private String markName;

    public String getMarkName() {
        return markName;
    }

    public void setMarkName(String markName) {
        this.markName = markName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMarkId() {
        return markId;
    }

    public void setMarkId(String markId) {
        this.markId = markId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getPatrol() {
        return patrol;
    }

    public void setPatrol(int patrol) {
        this.patrol = patrol;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getProjectGuid() {
        return projectGuid;
    }

    public void setProjectGuid(String projectGuid) {
        this.projectGuid = projectGuid;
    }

    public String getPatrolName() {
        return patrolName;
    }

    public void setPatrolName(String patrolName) {
        this.patrolName = patrolName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}

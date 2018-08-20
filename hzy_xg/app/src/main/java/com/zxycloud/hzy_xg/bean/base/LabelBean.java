package com.zxycloud.hzy_xg.bean.base;

public class LabelBean {
    /**
     * id : 5b084448bfda464fbbf2d8b0cfcedb4c
     * rid : XXXX1
     * name : lsname
     * describe : null
     * deviceParamterDesp : null
     * type : null
     * addTime : 2018-07-30 10:02:00
     * projectGuid : 343434343
     * installImgUrl : "sdkljflsjflksj"
     * addressDescribation : 诚盈中心14层1406
     */

    private String id;
    private String rid;
    private String name;
    private Object describe;
    private Object deviceParamterDesp;
    private Object type;
    private String addTime;
    private String projectGuid;
    private String installImgUrl;
    private String addressDescribation;

    public String getInstallImgUrl() {
        return installImgUrl;
    }

    public void setInstallImgUrl(String installImgUrl) {
        this.installImgUrl = installImgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getDescribe() {
        return describe;
    }

    public void setDescribe(Object describe) {
        this.describe = describe;
    }

    public Object getDeviceParamterDesp() {
        return deviceParamterDesp;
    }

    public void setDeviceParamterDesp(Object deviceParamterDesp) {
        this.deviceParamterDesp = deviceParamterDesp;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
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

    public String getAddressDescribation() {
        return addressDescribation;
    }

    public void setAddressDescribation(String addressDescribation) {
        this.addressDescribation = addressDescribation;
    }
}

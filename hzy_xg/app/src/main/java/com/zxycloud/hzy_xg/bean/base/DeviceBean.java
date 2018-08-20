package com.zxycloud.hzy_xg.bean.base;

public class DeviceBean {
    /**
     * deviceType : 点型光电感烟火灾探测器
     * deviceAddress : 0-#-#-#(18062801010100367078)
     * deviceGuid : 9411afd6e4c346e6957ff9c34fb3ab42
     * installlocation : 2楼办公室
     * adapterName : 01_NB_18062801010100367078NB设备
     * deviceName : 点型光电感烟火灾探测器
     * deviceStateName: 正常
     * deviceState: 1
     * deviceNumber: 1
     * vendor: 1
     */

    private String deviceType;
    private String deviceAddress;
    private String deviceGuid;
    private String installlocation;
    private String adapterName;
    private String deviceName;
    private String deviceStateName;
    private String vendor;
    private String deviceNumber;
    private int deviceState;


    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public String getVendor() {

        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getDeviceStateName() {
        return deviceStateName;
    }

    public void setDeviceStateName(String deviceStateName) {
        this.deviceStateName = deviceStateName;
    }

    public int getDeviceState() {
        return deviceState;
    }

    public void setDeviceState(int deviceState) {
        this.deviceState = deviceState;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public String getDeviceGuid() {
        return deviceGuid;
    }

    public void setDeviceGuid(String deviceGuid) {
        this.deviceGuid = deviceGuid;
    }

    public String getInstalllocation() {
        return installlocation;
    }

    public void setInstalllocation(String installlocation) {
        this.installlocation = installlocation;
    }

    public String getAdapterName() {
        return adapterName;
    }

    public void setAdapterName(String adapterName) {
        this.adapterName = adapterName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}

package com.smart.cloud.fire.global;

/**
 * Created by Rain on 2018/6/12.
 */
public class DeviceType {
    private int deviceId=1;

    public DeviceType(int deviceId, String deviceName) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
    }

    private String deviceName="";

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}

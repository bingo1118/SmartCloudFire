package com.smart.cloud.fire.pushmessage;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/25.
 */
public class GetUserAlarm implements Serializable{

    /**
     * address : 中国广东省深圳市宝安区X880(长凤路)
     * alarmSerialNumber : 2016-10-25 17:06:15:986--13622215085
     * alarmTime : 2016-10-25 17:06:15:986
     * alarmType : 3
     * areaName : 南凤派出所辖区
     * callerId : 13622215085
     * info : 绝色赌妃究竟是
     * latitude : 22.727248
     * longitude : 113.946579
     * smoke : 29461730
     */

    private String address;
    private String alarmSerialNumber;
    private String alarmTime;
    private int alarmType;
    private String areaName;
    private String callerId;
    private String info;
    private String latitude;
    private String longitude;
    private String smoke;
    private String callerName;
    private int deviceType;

    public GetUserAlarm() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAlarmSerialNumber() {
        return alarmSerialNumber;
    }

    public void setAlarmSerialNumber(String alarmSerialNumber) {
        this.alarmSerialNumber = alarmSerialNumber;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public int getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(int alarmType) {
        this.alarmType = alarmType;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCallerId() {
        return callerId;
    }

    public void setCallerId(String callerId) {
        this.callerId = callerId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getSmoke() {
        return smoke;
    }

    public void setSmoke(String smoke) {
        this.smoke = smoke;
    }

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

}

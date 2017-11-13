package com.smart.cloud.fire.activity.NFCDev;

import java.io.Serializable;

/**
 * Created by Rain on 2017/8/17.
 */
public class NFCRecordBean implements Serializable{
    String uid;
    String memo;
    String deviceName;
    String areaName;
    String devicestate;
    String address;
    String deviceType;
    String areaid;
    String longitude;
    String latitude;
    String addTime;
    String deviceTypeName;
    String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }



    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getDevicestate() {
        return devicestate;
    }

    public void setDevicestate(String devicestate) {
        this.devicestate = devicestate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
}

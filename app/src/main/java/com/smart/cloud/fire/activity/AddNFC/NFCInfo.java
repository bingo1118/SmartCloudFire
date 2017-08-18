package com.smart.cloud.fire.activity.AddNFC;

/**
 * Created by Rain on 2017/8/16.
 */
public class NFCInfo {

    String uid;
    String lon;
    String lat;
    String areaId;
    String areaName;
    String deviceTypeId;
    String deviceTypeName;
    String deviceName;
    String address;

    public NFCInfo() {
    }

    public NFCInfo(String uid, String lon, String lat, String areaId, String areaName, String deviceTypeId, String deviceTypeName, String deviceName, String address) {

        this.uid = uid;
        this.lon = lon;
        this.lat = lat;
        this.areaId = areaId;
        this.areaName = areaName;
        this.deviceTypeId = deviceTypeId;
        this.deviceTypeName = deviceTypeName;
        this.deviceName = deviceName;
        this.address = address;
    }

    public String getUid() {

        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(String deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

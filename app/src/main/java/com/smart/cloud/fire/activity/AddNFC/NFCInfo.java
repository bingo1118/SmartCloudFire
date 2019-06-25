package com.smart.cloud.fire.activity.AddNFC;

/**
 * Created by Rain on 2017/8/16.
 */
public class NFCInfo {

    String uid;//设备ID
    String lon;//经度
    String lat;//纬度
    String areaId;//区域ID
    String areaName;//区域名
    String deviceTypeId;//设备类型号
    String deviceTypeName;//设备类型名称
    String deviceName;//设备名称
    String address;//设备地址

    String memo;//备注
    String userId;//添加人账号
    String addTime;//添加时间
    String producer;//厂家
    String makeTime;//生产日期
    String overTime;//过期日期
    String workerName;//巡检人
    String workerPhone;//巡检人账号
    String makeAddress;//生产地址

    private String pid; //属于哪个巡检点

    private String tid;//属于哪次任务
    private String ischeck; //是否检查
    private String qualified; //是否合格

    public String getMakeAddress() {
        return makeAddress;
    }

    public void setMakeAddress(String makeAddress) {
        this.makeAddress = makeAddress;
    }



    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getWorkerPhone() {
        return workerPhone;
    }

    public void setWorkerPhone(String workerPhone) {
        this.workerPhone = workerPhone;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getOverTime() {
        return overTime;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }

    public String getMakeTime() {
        return makeTime;
    }

    public void setMakeTime(String makeTime) {
        this.makeTime = makeTime;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }



    public NFCInfo() {
    }

    public NFCInfo(String uid, String lon, String lat, String areaId, String areaName, String deviceTypeId,
                   String deviceTypeName, String deviceName, String address,String producer,String makeTime,
                    String makeAddress,String workerPhone) {

        this.uid = uid;
        this.lon = lon;
        this.lat = lat;
        this.areaId = areaId;
        this.areaName = areaName;
        this.deviceTypeId = deviceTypeId;
        this.deviceTypeName = deviceTypeName;
        this.deviceName = deviceName;
        this.address = address;
        this.producer=producer;
        this.makeTime=makeTime;
        this.makeAddress=makeAddress;
        this.workerPhone=workerPhone;
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

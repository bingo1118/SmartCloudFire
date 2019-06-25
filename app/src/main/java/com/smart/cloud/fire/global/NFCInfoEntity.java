package com.smart.cloud.fire.global;

public class NFCInfoEntity {
    private String uid;		//nfc   MAC
    private String areaId;	//所属区域ID
    private String areaName;	// 所属区域名称
    private String deviceType;	//设备类型ID
    private String deviceTypeName;	//设备类型ID
    private String deviceName;	//设备名称
    private String address;		//地址
    private String longitude;	//经度
    private String latitude;	//纬度
    private String addTime;		//	时间（设备录入时间或者巡检时间）安装日期
    private String memo;		//	备注信息
    private Integer devicestate = 0;	//设备状态，0待检查，1、合格、2、不合格
    private String userId;		//检查用户
    private String photo1;

    private String producer;	//厂家
    private String makeTime;	//生产日期
    private String overTime;	//过期日期
    private String workerName;	//巡检人
    private String workerPhone;	//巡检人联系方式
    private String endTime;		//最后一次巡检时间

    private String pid; //属于哪个巡检点

    private String tid;//属于哪次任务
    private String ischeck; //是否检查
    private String qualified; //是否合格

    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Integer getDevicestate() {
        return devicestate;
    }
    public void setDevicestate(Integer devicestate) {
        this.devicestate = devicestate;
    }
    public String getAreaName() {
        return areaName;
    }
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getAreaId() {
        return areaId;
    }
    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
    public String getDeviceType() {
        return deviceType;
    }
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
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
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getDeviceTypeName() {
        return deviceTypeName;
    }
    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }
    public String getPhoto1() {
        return photo1;
    }
    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }
    public String getProducer() {
        return producer;
    }
    public void setProducer(String producer) {
        this.producer = producer;
    }
    public String getMakeTime() {
        return makeTime;
    }
    public void setMakeTime(String makeTime) {
        this.makeTime = makeTime;
    }
    public String getOverTime() {
        return overTime;
    }
    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }
    public String getWorkerName() {
        return workerName;
    }
    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }
    public String getWorkerPhone() {
        return workerPhone;
    }
    public void setWorkerPhone(String workerPhone) {
        this.workerPhone = workerPhone;
    }
    public String getPid() {
        return pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
    public String getTid() {
        return tid;
    }
    public void setTid(String tid) {
        this.tid = tid;
    }
    public String getIscheck() {
        return ischeck;
    }
    public void setIscheck(String ischeck) {
        this.ischeck = ischeck;
    }
    public String getQualified() {
        return qualified;
    }
    public void setQualified(String qualified) {
        this.qualified = qualified;
    }
}

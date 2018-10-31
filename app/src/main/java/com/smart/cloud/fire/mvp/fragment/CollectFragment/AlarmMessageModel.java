package com.smart.cloud.fire.mvp.fragment.CollectFragment;

import org.litepal.crud.LitePalSupport;

/**
 * Created by Administrator on 2016/9/23.
 */
public class AlarmMessageModel extends LitePalSupport {

    /**
     * address : 中国广东省广州市天河区黄埔大道西568号
     * alarmTime : 2016-09-23 11:25:54:258
     * alarmType : 202
     * areaName : 南凤派出所辖区
     * ifDealAlarm : 1
     * latitude : 23.131829
     * longitude : 113.35047
     * mac : 335f1730
     * name : 塘厦消防队
     * placeType :
     * placeeAddress :
     * principal1 : 古鑫子
     * principal1Phone : 13430128967
     * principal2 :
     * principal2Phone :
     */

    private String address;
    private String alarmTime;
    private int alarmType;
    private String alarmTypeName;
    private String areaName;
    private int ifDealAlarm;
    private String latitude;
    private String longitude;
    private String mac;
    private String name;
    private String placeType;
    private String placeeAddress;
    private String principal1;
    private String principal1Phone;
    private String principal2;
    private String principal2Phone;
    private int alarmFamily;
    private int deviceType;
    private String alarmFamilys;

    private String dealPeople;
    private String dealDetail;
    private int alarmTruth;
    private String image_path;
    private String video_path;
    private int id;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public int getIfDealAlarm() {
        return ifDealAlarm;
    }

    public void setIfDealAlarm(int ifDealAlarm) {
        this.ifDealAlarm = ifDealAlarm;
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

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public String getPlaceeAddress() {
        return placeeAddress;
    }

    public void setPlaceeAddress(String placeeAddress) {
        this.placeeAddress = placeeAddress;
    }

    public String getPrincipal1() {
        return principal1;
    }

    public void setPrincipal1(String principal1) {
        this.principal1 = principal1;
    }

    public String getPrincipal1Phone() {
        return principal1Phone;
    }

    public void setPrincipal1Phone(String principal1Phone) {
        this.principal1Phone = principal1Phone;
    }

    public String getPrincipal2() {
        return principal2;
    }

    public void setPrincipal2(String principal2) {
        this.principal2 = principal2;
    }

    public String getPrincipal2Phone() {
        return principal2Phone;
    }

    public void setPrincipal2Phone(String principal2Phone) {
        this.principal2Phone = principal2Phone;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public int getAlarmFamily() {
        return alarmFamily;
    }

    public void setAlarmFamily(int alarmFamily) {
        this.alarmFamily = alarmFamily;
    }

    public String getAlarmFamilys() {
        return alarmFamilys;
    }

    public void setAlarmFamilys(String alarmFamilys) {
        this.alarmFamilys = alarmFamilys;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getVideo_path() {
        return video_path;
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }

    public int getAlarmTruth() {
        return alarmTruth;
    }

    public void setAlarmTruth(int alarmTruth) {
        this.alarmTruth = alarmTruth;
    }

    public String getDealDetail() {
        return dealDetail;
    }

    public void setDealDetail(String dealDetail) {
        this.dealDetail = dealDetail;
    }

    public String getDealPeople() {
        return dealPeople;
    }

    public void setDealPeople(String dealPeople) {
        this.dealPeople = dealPeople;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlarmTypeName() {
        return alarmTypeName;
    }

    public void setAlarmTypeName(String alarmTypeName) {
        this.alarmTypeName = alarmTypeName;
    }
}

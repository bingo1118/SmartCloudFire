package com.smart.cloud.fire.global;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/18.
 */
public class NormalSmoke implements Serializable{


    /**
     * address : 中国广东省广州市天河区黄埔大道西580号
     * areaName : 天河区
     * ifDealAlarm : 1
     * latitude : 23.131724
     * longitude : 113.350573
     * mac : 5C2B0733
     * name : Gggg
     * netState : 0
     * placeType : 沐足
     * placeeAddress : 你自己不
     * principal1 : Fgf
     * principal1Phone :
     * principal2 :
     * principal2Phone :
     */
    private int type;
    private String address;
    private String areaName;
    private int ifDealAlarm;
    private double latitude;
    private double longitude;
    private String mac;
    private String name;
    private int netState;
    private String placeType;
    private String placeeAddress;
    private String principal1;
    private String principal1Phone;
    private String principal2;
    private String principal2Phone;
    private String repeater;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
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

    public int getNetState() {
        return netState;
    }

    public void setNetState(int netState) {
        this.netState = netState;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRepeater() {
        return repeater;
    }

    public void setRepeater(String repeater) {
        this.repeater = repeater;
    }
}

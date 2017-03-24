package com.smart.cloud.fire.mvp.fragment.MapFragment;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */
public class Smoke implements Serializable {

    /**
     * address : 中国广东省深圳市宝安区光侨路
     * areaName : 南凤派出所辖区
     * ifDealAlarm : 1
     * latitude : 22.732041
     * longitude : 113.94769
     * mac : 03091620
     * name : 朱氏猪脚饭
     * netState : 1
     * placeType : 餐饮店
     * placeeAddress :
     * principal1 : 罗海琼
     * principal1Phone : 13724309809
     * principal2 :
     * principal2Phone :
     * repeater
     */

    private int type=2;
    private String address;
    private String areaName;
    private int ifDealAlarm;
    private String latitude;
    private String longitude;
    private String mac;
    private String name;
    private int netState;
    private String placeType;
    private String placeeAddress;
    private String principal1;
    private String principal1Phone;
    private String principal2;
    private String principal2Phone;
    private List<String> repeaters;
    private String repeater;
    private Camera camera;
    private int areaId;
    private int deviceType;

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

    public List<String> getRepeaters() {
        return repeaters;
    }

    public void setRepeaters(List<String> repeaters) {
        this.repeaters = repeaters;
    }

    public String getRepeater() {
        return repeater;
    }

    public void setRepeater(String repeater) {
        this.repeater = repeater;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }
}

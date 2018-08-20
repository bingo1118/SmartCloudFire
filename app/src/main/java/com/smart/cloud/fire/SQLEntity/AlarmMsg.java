package com.smart.cloud.fire.SQLEntity;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Created by Rain on 2018/8/20.
 */
public class AlarmMsg extends LitePalSupport {

//    @Column(unique = true)
//    private String id;
    private String smokeMac;
    private String dealUserId;
    private String dealUserName;
    private String ifDealAlarm;
    private String dealTime;
    private String alarmType;
    private String alarmTime;
    private String dealPeople;
    private String dealDetail;
    private String alarmTruth;
    private String alarmFamily;
    private String repeaterMac;


//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public String getSmokeMac() {
        return smokeMac;
    }

    public void setSmokeMac(String smokeMac) {
        this.smokeMac = smokeMac;
    }

    public String getDealUserId() {
        return dealUserId;
    }

    public void setDealUserId(String dealUserId) {
        this.dealUserId = dealUserId;
    }

    public String getDealUserName() {
        return dealUserName;
    }

    public void setDealUserName(String dealUserName) {
        this.dealUserName = dealUserName;
    }

    public String getIfDealAlarm() {
        return ifDealAlarm;
    }

    public void setIfDealAlarm(String ifDealAlarm) {
        this.ifDealAlarm = ifDealAlarm;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getDealPeople() {
        return dealPeople;
    }

    public void setDealPeople(String dealPeople) {
        this.dealPeople = dealPeople;
    }

    public String getDealDetail() {
        return dealDetail;
    }

    public void setDealDetail(String dealDetail) {
        this.dealDetail = dealDetail;
    }

    public String getAlarmTruth() {
        return alarmTruth;
    }

    public void setAlarmTruth(String alarmTruth) {
        this.alarmTruth = alarmTruth;
    }

    public String getAlarmFamily() {
        return alarmFamily;
    }

    public void setAlarmFamily(String alarmFamily) {
        this.alarmFamily = alarmFamily;
    }

    public String getRepeaterMac() {
        return repeaterMac;
    }

    public void setRepeaterMac(String repeaterMac) {
        this.repeaterMac = repeaterMac;
    }

}

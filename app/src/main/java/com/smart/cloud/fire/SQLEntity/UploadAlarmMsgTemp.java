package com.smart.cloud.fire.SQLEntity;

import org.litepal.crud.LitePalSupport;

/**
 * Created by Rain on 2018/8/20.
 */
public class UploadAlarmMsgTemp extends LitePalSupport {
    String userID;
    String mac;
    String privilege;
    String alarmTruth;
    String dealDetail;
    String image_path;
    String video_path;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getAlarmTruth() {
        return alarmTruth;
    }

    public void setAlarmTruth(String alarmTruth) {
        this.alarmTruth = alarmTruth;
    }

    public String getDealDetail() {
        return dealDetail;
    }

    public void setDealDetail(String dealDetail) {
        this.dealDetail = dealDetail;
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
}

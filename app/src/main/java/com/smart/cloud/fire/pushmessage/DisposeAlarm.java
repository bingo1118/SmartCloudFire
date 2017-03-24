package com.smart.cloud.fire.pushmessage;

/**
 * Created by Administrator on 2016/10/25.
 */
public class DisposeAlarm {

    /**
     * alarmType : 4
     * police : 13622215085
     * time : 2016-10-25 17:57:41:025
     */

    private int alarmType;
    private String police;
    private String time;
    private String policeName;
    private int deviceType;

    public int getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(int alarmType) {
        this.alarmType = alarmType;
    }

    public String getPolice() {
        return police;
    }

    public void setPolice(String police) {
        this.police = police;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPoliceName() {
        return policeName;
    }

    public void setPoliceName(String policeName) {
        this.policeName = policeName;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }
}

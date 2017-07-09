package com.smart.cloud.fire.pushmessage;

import java.io.Serializable;

/**
 * @@有线烟感报警信息
 * Created by Rain on 2017/6/30.
 */
public class PushWiredSmokeAlarmMsg implements Serializable {
    private String faultCode;
    private String faultDevDesc;
    private String faultInfo;
    private String faultTime;
    private String faultType;
    private String repeater;

    public String getFaultCode() {
        return faultCode;
    }

    public void setFaultCode(String faultCode) {
        this.faultCode = faultCode;
    }

    public String getFaultDevDesc() {
        return faultDevDesc;
    }

    public void setFaultDevDesc(String faultDevDesc) {
        this.faultDevDesc = faultDevDesc;
    }

    public String getFaultInfo() {
        return faultInfo;
    }

    public void setFaultInfo(String faultInfo) {
        this.faultInfo = faultInfo;
    }

    public String getFaultTime() {
        return faultTime;
    }

    public void setFaultTime(String faultTime) {
        this.faultTime = faultTime;
    }

    public String getFaultType() {
        return faultType;
    }

    public void setFaultType(String faultType) {
        this.faultType = faultType;
    }

    public String getRepeater() {
        return repeater;
    }

    public void setRepeater(String repeater) {
        this.repeater = repeater;
    }
}

package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.WiredDevFragment;

import java.io.Serializable;

/**
 * Created by Rain on 2017/6/30.
 */
public class WiredSmoke implements Serializable {
    private String repeaterMac;
    private String faultInfo;
    private String faultType;
    private String faultCode;
    private String faultDevDesc;
    private String faultTime;
    private String hostType;




    public String getFaultInfo() {
        return faultInfo;
    }

    public void setFaultInfo(String faultInfo) {
        this.faultInfo = faultInfo;
    }

    public String getFaultType() {
        return faultType;
    }

    public void setFaultType(String faultType) {
        this.faultType = faultType;
    }



    public String getFaultDevDesc() {
        return faultDevDesc;
    }

    public void setFaultDevDesc(String faultDevDesc) {
        this.faultDevDesc = faultDevDesc;
    }

    public String getFaultTime() {
        return faultTime;
    }

    public void setFaultTime(String faultTime) {
        this.faultTime = faultTime;
    }

    public String getRepeaterMac() {
        return repeaterMac;
    }

    public void setRepeaterMac(String repeaterMac) {
        this.repeaterMac = repeaterMac;
    }

    public String getFaultCode() {
        return faultCode;
    }

    public void setFaultCode(String faultCode) {
        this.faultCode = faultCode;
    }

    public String getHostType() {
        return hostType;
    }

    public void setHostType(String hostType) {
        this.hostType = hostType;
    }
}

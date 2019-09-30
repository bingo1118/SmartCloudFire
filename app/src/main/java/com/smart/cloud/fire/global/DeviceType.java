package com.smart.cloud.fire.global;

/**
 * Created by Rain on 2018/6/12.
 */
public class DeviceType {

    private int errorCode;
    private String error;
    private String mac;
    private String devType;
    private String devTypeName;
    private int electrState;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public int getElectrState() {
        return electrState;
    }

    public void setElectrState(int electrState) {
        this.electrState = electrState;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDevTypeName() {
        return devTypeName;
    }

    public void setDevTypeName(String devTypeName) {
        this.devTypeName = devTypeName;
    }
}

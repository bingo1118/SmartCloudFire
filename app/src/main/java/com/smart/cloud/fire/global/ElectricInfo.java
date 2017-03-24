package com.smart.cloud.fire.global;

import java.util.List;

/**
 * Created by Administrator on 2016/11/15.
 */
public class ElectricInfo<T> {

    /**
     * error : 获取烟感成功）
     * errorCode : 0
     */
    private String error;
    private int errorCode;
    private List<T> Electric;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public List<T> getElectric() {
        return Electric;
    }

    public void setElectric(List<T> electric) {
        Electric = electric;
    }
}

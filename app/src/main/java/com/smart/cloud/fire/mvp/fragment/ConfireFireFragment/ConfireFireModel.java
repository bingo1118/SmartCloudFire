package com.smart.cloud.fire.mvp.fragment.ConfireFireFragment;

import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;

/**
 * Created by Administrator on 2016/9/23.
 */
public class ConfireFireModel {

    /**
     * error : 获取烟感成功
     * errorCode : 0
     */

    private String error;
    private int errorCode;
    private Smoke smoke;

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

    public Smoke getSmoke() {
        return smoke;
    }

    public void setSmoke(Smoke smoke) {
        this.smoke = smoke;
    }
}

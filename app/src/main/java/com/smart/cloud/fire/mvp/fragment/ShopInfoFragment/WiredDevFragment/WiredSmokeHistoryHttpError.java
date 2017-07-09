package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.WiredDevFragment;

import com.smart.cloud.fire.global.ElectricInfo;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.mvp.fragment.CollectFragment.AlarmMessageModel;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Camera;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rain on 2017/7/3.
 */
public class WiredSmokeHistoryHttpError<T> {
    private String error;
    private int errorCode;
    private List<WiredSmokeHistory> alarm;

    public List<WiredSmokeHistory> getAlarm() {
        return alarm;
    }

    public void setAlarm(List<WiredSmokeHistory> alarm) {
        this.alarm = alarm;
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
}

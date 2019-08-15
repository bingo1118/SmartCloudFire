package com.smart.cloud.fire.Volley;


import com.smart.cloud.fire.mvp.fragment.CollectFragment.AlarmMessageModel;

public class VolleyBaseBean<T> {
    private String error;
    private int errorCode;
    private AlarmMessageModel lasteatAlarm;

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


    public AlarmMessageModel getLasteatAlarm() {
        return lasteatAlarm;
    }

    public void setLasteatAlarm(AlarmMessageModel lasteatAlarm) {
        this.lasteatAlarm = lasteatAlarm;
    }
}

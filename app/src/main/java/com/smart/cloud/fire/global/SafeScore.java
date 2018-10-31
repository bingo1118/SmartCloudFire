package com.smart.cloud.fire.global;

/**
 * Created by Rain on 2018/10/24.
 */
public class SafeScore {

    private float safeScore;
    private float offline;
    private float realtimeAlarm;
    private float historiAlarm;
    private String error;
    private int errorCode;

    private int totalSum;//设备总数
    private int historiAlarmSum;//历史报警高频设备数
    private int realtimeAlarmSum;//实时报警设备数
    private int offlineSum;//离线设备数

    public int getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(int totalSum) {
        this.totalSum = totalSum;
    }

    public int getHistoriAlarmSum() {
        return historiAlarmSum;
    }

    public void setHistoriAlarmSum(int historiAlarmSum) {
        this.historiAlarmSum = historiAlarmSum;
    }

    public int getRealtimeAlarmSum() {
        return realtimeAlarmSum;
    }

    public void setRealtimeAlarmSum(int realtimeAlarmSum) {
        this.realtimeAlarmSum = realtimeAlarmSum;
    }

    public int getOfflineSum() {
        return offlineSum;
    }

    public void setOfflineSum(int offlineSum) {
        this.offlineSum = offlineSum;
    }

    public float getSafeScore() {
        return safeScore;
    }

    public void setSafeScore(float safeScore) {
        this.safeScore = safeScore;
    }

    public float getOffline() {
        return offline;
    }

    public void setOffline(float offline) {
        this.offline = offline;
    }

    public float getRealtimeAlarm() {
        return realtimeAlarm;
    }

    public void setRealtimeAlarm(float realtimeAlarm) {
        this.realtimeAlarm = realtimeAlarm;
    }

    public float getHistoriAlarm() {
        return historiAlarm;
    }

    public void setHistoriAlarm(float historiAlarm) {
        this.historiAlarm = historiAlarm;
    }

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
}

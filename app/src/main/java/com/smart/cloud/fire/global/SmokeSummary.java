package com.smart.cloud.fire.global;

/**
 * Created by Administrator on 2016/11/2.
 */
public class SmokeSummary {

    /**
     * allSmokeNumber : 264
     * error : 成功
     * errorCode : 0
     * lossSmokeNumber : 0
     * onlineSmokeNumber : 264
     */

    private int allSmokeNumber;
    private String error;
    private int errorCode;
    private int lossSmokeNumber;
    private int onlineSmokeNumber;

    public int getAllSmokeNumber() {
        return allSmokeNumber;
    }

    public void setAllSmokeNumber(int allSmokeNumber) {
        this.allSmokeNumber = allSmokeNumber;
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

    public int getLossSmokeNumber() {
        return lossSmokeNumber;
    }

    public void setLossSmokeNumber(int lossSmokeNumber) {
        this.lossSmokeNumber = lossSmokeNumber;
    }

    public int getOnlineSmokeNumber() {
        return onlineSmokeNumber;
    }

    public void setOnlineSmokeNumber(int onlineSmokeNumber) {
        this.onlineSmokeNumber = onlineSmokeNumber;
    }
}

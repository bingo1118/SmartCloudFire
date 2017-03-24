package com.smart.cloud.fire.global;

import java.util.List;

/**
 * Created by Administrator on 2016/11/15.
 */
public class TemperatureTime {


    /**
     * Electric : [{"electricTime":"2016-11-15 14:45:16","electricValue":"224.649994"},{"electricTime":"2016-11-15 14:43:27","electricValue":"224.880005"},{"electricTime":"2016-11-15 14:41:38","electricValue":"224.910004"},{"electricTime":"2016-11-15 14:39:49","electricValue":"225.410004"},{"electricTime":"2016-11-15 14:35:17","electricValue":"224.039993"},{"electricTime":"2016-11-15 14:33:28","electricValue":"223.229996"}]
     * error : 获取电气属性信息成功
     * errorCode : 0
     */

    private String error;
    private int errorCode;
    /**
     * electricTime : 2016-11-15 14:45:16
     * electricValue : 224.649994
     */

    private List<ElectricBean> Electric;

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

    public List<ElectricBean> getElectric() {
        return Electric;
    }

    public void setElectric(List<ElectricBean> Electric) {
        this.Electric = Electric;
    }

    public static class ElectricBean {
        private String electricTime;
        private String electricValue;

        public String getElectricTime() {
            return electricTime;
        }

        public void setElectricTime(String electricTime) {
            this.electricTime = electricTime;
        }

        public String getElectricValue() {
            return electricValue;
        }

        public void setElectricValue(String electricValue) {
            this.electricValue = electricValue;
        }
    }
}

package com.smart.cloud.fire.global;

import java.util.List;

/**
 * Created by Administrator on 2016/12/27.
 */
public class Test {


    /**
     * error : 获取单个电气设备成功
     * errorCode : 0
     * electric : [{"electricTime":"2017-01-09 17:24:51","electricType":8,"electricValue":[{"id":1,"value":"0.000","ElectricThreshold":"500"}]},{"electricTime":"2017-01-09 17:24:51","electricType":9,"electricValue":[{"id":1,"value":"27.96","ElectricThreshold":"60.00"},{"id":2,"value":"28.73","ElectricThreshold":"60.00"},{"id":3,"value":"27.90","ElectricThreshold":"60.00"},{"id":4,"value":"26.93","ElectricThreshold":"60.00"}]},{"electricTime":"2017-01-09 17:24:51","electricType":6,"electricValue":[{"id":1,"value":"0.00","ElectricThreshold":"187\\242"},{"id":2,"value":"0.00","ElectricThreshold":"187\\242"},{"id":3,"value":"0.00","ElectricThreshold":"187\\242"}]},{"electricTime":"2017-01-09 17:24:51","electricType":7,"electricValue":[{"id":1,"value":"0.000","ElectricThreshold":"6"},{"id":2,"value":"0.000","ElectricThreshold":"6"},{"id":3,"value":"0.000","ElectricThreshold":"6"},{"id":4,"value":"0.000","ElectricThreshold":"6"}]}]
     */

    private String error;
    private int errorCode;
    /**
     * electricTime : 2017-01-09 17:24:51
     * electricType : 8
     * electricValue : [{"id":1,"value":"0.000","ElectricThreshold":"500"}]
     */

    private List<ElectricBean> electric;

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
        return electric;
    }

    public void setElectric(List<ElectricBean> electric) {
        this.electric = electric;
    }

    public static class ElectricBean {
        private String electricTime;
        private int electricType;
        /**
         * id : 1
         * value : 0.000
         * ElectricThreshold : 500
         */

        private List<ElectricValueBean> electricValue;

        public String getElectricTime() {
            return electricTime;
        }

        public void setElectricTime(String electricTime) {
            this.electricTime = electricTime;
        }

        public int getElectricType() {
            return electricType;
        }

        public void setElectricType(int electricType) {
            this.electricType = electricType;
        }

        public List<ElectricValueBean> getElectricValue() {
            return electricValue;
        }

        public void setElectricValue(List<ElectricValueBean> electricValue) {
            this.electricValue = electricValue;
        }

        public static class ElectricValueBean {
            private int id;
            private String value;
            private String ElectricThreshold;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getElectricThreshold() {
                return ElectricThreshold;
            }

            public void setElectricThreshold(String ElectricThreshold) {
                this.ElectricThreshold = ElectricThreshold;
            }
        }
    }
}

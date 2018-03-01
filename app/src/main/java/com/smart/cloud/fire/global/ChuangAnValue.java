package com.smart.cloud.fire.global;

import java.util.List;

/**
 * Created by Rain on 2018/2/28.
 */
public class ChuangAnValue {
    private List<ChuangAnValueBean> chuanganValue;

    public List<ChuangAnValueBean> getChuanganValue() {
        return chuanganValue;
    }

    public void setChuanganValue(List<ChuangAnValueBean> chuanganValue) {
        this.chuanganValue = chuanganValue;
    }

    public class ChuangAnValueBean {
        private String state;
        private int id;
        private String value;
        private String time;




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

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }
}

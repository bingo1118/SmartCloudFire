package com.smart.cloud.fire.order.OrderInfoDetail;

import com.smart.cloud.fire.order.OrderInfo;

import java.util.List;

public class HttpOrderInfoEntity {

    private String error;
    private int errorCode;
    private List<OrderInfo> list;


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

    public List<OrderInfo> getList() {
        return list;
    }

    public void setList(List<OrderInfo> list) {
        this.list = list;
    }
}

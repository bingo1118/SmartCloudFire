package com.smart.cloud.fire.order.OrderList;

import com.smart.cloud.fire.order.JobOrder;

import java.util.List;

public class HttpOrderListEntity {

    private String error;
    private int errorCode;
    private List<JobOrder> list;

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


    public List<JobOrder> getList() {
        return list;
    }

    public void setList(List<JobOrder> list) {
        this.list = list;
    }
}

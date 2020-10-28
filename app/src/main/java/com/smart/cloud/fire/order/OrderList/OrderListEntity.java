package com.smart.cloud.fire.order.OrderList;

import com.smart.cloud.fire.order.JobOrder;

import java.util.List;

public interface OrderListEntity {
    void getDataSuccess(List<JobOrder> smokeList);
    void showLoading();
    void hideLoading();
    void unSubscribe(String type);
    void getDataFail(String msg);
}

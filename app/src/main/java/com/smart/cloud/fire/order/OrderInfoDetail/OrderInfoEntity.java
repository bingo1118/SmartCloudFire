package com.smart.cloud.fire.order.OrderInfoDetail;

import com.smart.cloud.fire.order.OrderInfo;

import java.util.List;

public interface OrderInfoEntity {

    void getDataSuccess(List<OrderInfo> smokeList);
    void showLoading();
    void hideLoading();
    void unSubscribe(String type);
    void getDataFail(String msg);
}

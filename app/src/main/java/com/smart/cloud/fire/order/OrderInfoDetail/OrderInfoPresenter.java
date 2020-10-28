package com.smart.cloud.fire.order.OrderInfoDetail;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.order.OrderInfo;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class OrderInfoPresenter extends BasePresenter<OrderInfoEntity> {


    public OrderInfoPresenter(OrderInfoEntity view) {
        attachView(view);
    }

    public void getOrderInfo(String jkey) {
        mvpView.showLoading();
        Observable mObservable;
        mObservable = apiStores1.getOrderDetail(jkey);

        addSubscription(mObservable, new SubscriberCallBack<>(new ApiCallback<HttpOrderInfoEntity>() {
            @Override
            public void onSuccess(HttpOrderInfoEntity model) {
                int result = model.getErrorCode();
                if (result == 0) {
                    List<OrderInfo> smokeList = model.getList();
                    mvpView.getDataSuccess(smokeList);
                } else {
                    List<OrderInfo> mSmokeList = new ArrayList<>();
                    mvpView.getDataSuccess(mSmokeList);
                    mvpView.getDataFail("无数据");
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                List<OrderInfo> mList = new ArrayList<>();
                mvpView.getDataSuccess(mList);
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }

}

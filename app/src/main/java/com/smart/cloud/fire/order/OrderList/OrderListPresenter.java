package com.smart.cloud.fire.order.OrderList;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.order.JobOrder;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class OrderListPresenter extends BasePresenter<OrderListEntity> {


    public OrderListPresenter(OrderListEntity view) {
        attachView(view);
    }

    public void getAllDev(String userId) {
        mvpView.showLoading();
        Observable mObservable;
        mObservable = apiStores1.getAllOrder(userId,MyApp.getPrivilege()+"");

        addSubscription(mObservable, new SubscriberCallBack<>(new ApiCallback<HttpOrderListEntity>() {
            @Override
            public void onSuccess(HttpOrderListEntity model) {
                int result = model.getErrorCode();
                if (result == 0) {
                    List<JobOrder> smokeList = model.getList();
                    mvpView.getDataSuccess(smokeList);
                } else {
                    List<JobOrder> mSmokeList = new ArrayList<>();
                    mvpView.getDataSuccess(mSmokeList);
                    mvpView.getDataFail("无数据");
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                List<JobOrder> mList = new ArrayList<>();
                mvpView.getDataSuccess(mList);
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }

}
